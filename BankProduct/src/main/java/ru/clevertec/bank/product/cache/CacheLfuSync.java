package ru.clevertec.bank.product.cache;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс реализующий кэш, для хранения сущностей по алгоритму LFU.
 *
 * @param <V> тип кэшируемой сущности
 */
@Slf4j
public class CacheLfuSync<V> implements Cacheable<Long, V> {

    private final int maxSize;
    private String entityRepo;
    private List<Node> nodeList;
    private Map<Long, Node> store = new ConcurrentHashMap<>();
    private int qNode;
    private final Lock lock = new ReentrantLock();

    public CacheLfuSync(String entityRepo, int maxSize) {
        this.maxSize = maxSize;
        this.entityRepo = entityRepo;
        nodeList = new ArrayList<>(maxSize);
        qNode = 0;
        log.info("Cache Lfu has been created  for {}", entityRepo);
    }

    public V put(Long id, V value) {
        Node<V> node = store.get(id);
        if (Objects.nonNull(node)) {
            node.incrementFrequency();
            V val = node.getValue();
            node.setValue(value);
            return val;
        } else {
            node = new Node(id, value);
            store.put(id, node);
            lock.lock();
            try {
                if (qNode < maxSize) {
                    qNode++;
                    nodeList.add(node);
                } else {
                    int i = minFreqNode();
                    store.remove(nodeList.get(i).getKey());
                    nodeList.set(i, node);
                }
            } finally {
                lock.unlock();
            }
        }
        return null;
    }

    public V get(Object key) {
        Node<V> node = store.get(key);
        if (Objects.isNull(node)) {
            return null;
        }
        node.incrementFrequency();
        return node.getValue();
    }

    public V remove(Object key) {
        if (store.containsKey(key)) {
            Iterator<Node> iterator = nodeList.iterator();
            lock.lock();
            try {
                while (iterator.hasNext()) {
                    if ((Long) iterator.next().getKey() == key) {
                        iterator.remove();
                        qNode--;
                        break;
                    }
                }
            } finally {
                lock.unlock();
            }
            Node<V> value = store.remove(key);
            return value.getValue();
        }
        return null;
    }

    private int minFreqNode() {
        int index = 0;
        long minFrequency = nodeList.get(0).getFrequency();
        for (int i = 0; i < nodeList.size(); i++) {
            long fr = nodeList.get(i).getFrequency();
            if (minFrequency > fr) {
                minFrequency = fr;
                index = i;
            }
        }
        return index;
    }

    @Override
    public void setRepositoriesName(String name) {
        entityRepo = name;
    }

    /**
     * Дочерний класс для хранения кэшируемой сущности и частоты обращения к ней
     *
     * @param <V>
     */
    private class Node<V> {
        private AtomicLong frequency = new AtomicLong(1);
        private long key;
        private V value;

        public Node(long key, V value) {
            this.value = value;
            this.key = key;
        }

        public long getKey() {
            return key;
        }

        public long getFrequency() {
            return frequency.get();
        }

        public V getValue() {
            return value;
        }

        public void incrementFrequency() {
            frequency.incrementAndGet();
        }

        public void setValue(V value) {
            this.value = value;
        }
    }
}
