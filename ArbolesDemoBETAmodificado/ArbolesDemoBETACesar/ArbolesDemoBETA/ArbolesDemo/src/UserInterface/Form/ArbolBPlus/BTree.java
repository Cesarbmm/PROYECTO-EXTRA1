package UserInterface.Form.ArbolBPlus;

import static java.sql.DriverManager.println;
import java.util.Stack;

public class BTree<K extends Comparable, V> {

    public final static int REBALANCE_FOR_LEAF_NODE = 1;
    public final static int REBALANCE_FOR_INTERNAL_NODE = 2;

    private BTNode<K, V> mRoot = null;
    private long mSize = 0L;
    private BTNode<K, V> mIntermediateInternalNode = null;
    private int mNodeIdx = 0;
    private final Stack<StackInfo> mStackTracer = new Stack<StackInfo>();

    //
    // Get the root node
    //
    public BTNode<K, V> getRootNode() {
        return mRoot;
    }

    //
    // The total number of nodes in the tree
    //
    public long size() {
        return mSize;
    }

    //
    // Clear all the entries in the tree
    //
    public void clear() {
        mSize = 0L;
        mRoot = null;
    }

    //
    // Create a node with default values
    //
    private BTNode<K, V> createNode() {
        BTNode<K, V> btNode;
        btNode = new BTNode();
        btNode.mIsLeaf = true;
        btNode.mCurrentKeyNum = 0;
        return btNode;
    }

    //
    // Search value for a specified key of the tree
    //
    
    public V search(K key) {
        BTNode<K, V> currentNode = mRoot;
        BTKeyValue<K, V> currentKey;
        int i, numberOfKeys;

        while (currentNode != null) {
            numberOfKeys = currentNode.mCurrentKeyNum;
            i = 0;
            currentKey = currentNode.mKeys[i];

            System.out.println("Visita nodo: " + currentNode + " | número de claves: " + numberOfKeys);
            System.out.println("Clave que buscamos: " + key);

            while ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) > 0)) {
                System.out.println("Comprobando clave: " + currentKey.mKey);
                ++i;
                if (i < numberOfKeys) {
                    currentKey = currentNode.mKeys[i];
                } else {
                    --i;
                    break;
                }
            }

            if ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) == 0)) {
                System.out.println("Clave encontrada: " + currentKey.mKey);
                return currentKey.mValue;
            }

            // Para depuración: saber qué dirección estamos tomando en el árbol
            if (key.compareTo(currentKey.mKey) > 0) {
                System.out.println("Buscando a la derecha del nodo.");
                currentNode = BTNode.getRightChildAtIndex(currentNode, i);
            } else {
                System.out.println("Buscando a la izquierda del nodo.");
                currentNode = BTNode.getLeftChildAtIndex(currentNode, i);
            }
        }

        System.out.println("Clave no encontrada.");
        return null;
    }

    /*
    public V search(K key) {
    BTNode<K, V> currentNode = mRoot;
    BTKeyValue<K, V> currentKey;
    int i, numberOfKeys;

    while (currentNode != null) {
        numberOfKeys = currentNode.mCurrentKeyNum;
        i = 0;
        if (numberOfKeys > 0) {
            currentKey = currentNode.mKeys[i];
        } else {
            // Si el nodo no tiene claves, saltamos la búsqueda
            return null;
        }

        System.out.println("Visita nodo: " + currentNode + " | número de claves: " + numberOfKeys);
        System.out.println("Clave que buscamos: " + key);

        while ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) > 0)) {
            System.out.println("Comprobando clave: " + currentKey.mKey);
            ++i;
            if (i < numberOfKeys) {
                currentKey = currentNode.mKeys[i];
            } else {
                --i;
                break;
            }
        }

        if ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) == 0)) {
            System.out.println("Clave encontrada: " + currentKey.mKey);
            return currentKey.mValue;
        }

        // Para depuración: saber qué dirección estamos tomando en el árbol
        if (i < numberOfKeys && currentKey != null) {
            if (key.compareTo(currentKey.mKey) > 0) {
                System.out.println("Buscando a la derecha del nodo.");
                currentNode = BTNode.getRightChildAtIndex(currentNode, i);
            } else {
                System.out.println("Buscando a la izquierda del nodo.");
                currentNode = BTNode.getLeftChildAtIndex(currentNode, i);
            }
        } else {
            break; // Si currentKey es null o no hay más claves, no hay más nodos que explorar
        }
    }

    System.out.println("Clave no encontrada.");
    return null;
}
*/

    
    //
    // Insert key and its value into the tree
    //
    public BTree insert(K key, V value) {
        if (mRoot == null) {
            mRoot = createNode();
        }

        ++mSize;
        if (mRoot.mCurrentKeyNum == BTNode.UPPER_BOUND_KEYNUM) {
            // The root is full, split it
            BTNode<K, V> btNode = createNode();
            btNode.mIsLeaf = false;
            btNode.mChildren[0] = mRoot;
            mRoot = btNode;
            splitNode(mRoot, 0, btNode.mChildren[0]);
        }

        insertKeyAtNode(mRoot, key, value);
        return this;
    }

    //
    // Insert key and its value to the specified root
    //
    private void insertKeyAtNode(BTNode rootNode, K key, V value) {
        int i;
        int currentKeyNum = rootNode.mCurrentKeyNum;

        if (rootNode.mIsLeaf) {
            if (rootNode.mCurrentKeyNum == 0) {
                // Empty root
                rootNode.mKeys[0] = new BTKeyValue<K, V>(key, value);
                ++(rootNode.mCurrentKeyNum);
                return;
            }

            // Verify if the specified key doesn't exist in the node
            for (i = 0; i < rootNode.mCurrentKeyNum; ++i) {
                if (key.compareTo(rootNode.mKeys[i].mKey) == 0) {
                    // Find existing key, overwrite its value only
                    rootNode.mKeys[i].mValue = value;
                    --mSize;
                    return;
                }
            }

            i = currentKeyNum - 1;
            BTKeyValue<K, V> existingKeyVal = rootNode.mKeys[i];
            while ((i > -1) && (key.compareTo(existingKeyVal.mKey) < 0)) {
                rootNode.mKeys[i + 1] = existingKeyVal;
                --i;
                if (i > -1) {
                    existingKeyVal = rootNode.mKeys[i];
                }
            }

            i = i + 1;
            rootNode.mKeys[i] = new BTKeyValue<K, V>(key, value);

            ++(rootNode.mCurrentKeyNum);

            // Si el nodo hoja está lleno, dividirlo
            if (rootNode.mCurrentKeyNum == BTNode.UPPER_BOUND_KEYNUM) {
                splitLeafNode(rootNode);
            }

            return;
        }

        // This is an internal node (i.e: not a leaf node)
        // So let find the child node where the key is supposed to belong
        i = 0;
        int numberOfKeys = rootNode.mCurrentKeyNum;
        BTKeyValue<K, V> currentKey = rootNode.mKeys[i];
        while ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) > 0)) {
            ++i;
            if (i < numberOfKeys) {
                currentKey = rootNode.mKeys[i];
            } else {
                --i;
                break;
            }
        }

        if ((i < numberOfKeys) && (key.compareTo(currentKey.mKey) == 0)) {
            // The key already existed so replace its value and done with it
            currentKey.mValue = value;
            --mSize;
            return;
        }

        BTNode<K, V> btNode;
        if (key.compareTo(currentKey.mKey) > 0) {
            btNode = BTNode.getRightChildAtIndex(rootNode, i);
            i = i + 1;
        } else {
            if ((i - 1 >= 0) && (key.compareTo(rootNode.mKeys[i - 1].mKey) > 0)) {
                btNode = BTNode.getRightChildAtIndex(rootNode, i - 1);
            } else {
                btNode = BTNode.getLeftChildAtIndex(rootNode, i);
            }
        }

        if (btNode.mCurrentKeyNum == BTNode.UPPER_BOUND_KEYNUM) {
            // If the child node is a full node then handle it by splitting out
            // then insert key starting at the root node after splitting node
            splitNode(rootNode, i, btNode);
            insertKeyAtNode(rootNode, key, value);
            return;
        }

        insertKeyAtNode(btNode, key, value);
    }

    //
    // Split a child with respect to its parent at a specified node
    //
    private void splitNode(BTNode parentNode, int nodeIdx, BTNode btNode) {
        int i;

        BTNode<K, V> newNode = createNode();

        newNode.mIsLeaf = btNode.mIsLeaf;

        // Since the node is full,
        // new node must share LOWER_BOUND_KEYNUM (aka t - 1) keys from the node
        newNode.mCurrentKeyNum = BTNode.LOWER_BOUND_KEYNUM;

        // Copy right half of the keys from the node to the new node
        for (i = 0; i < BTNode.LOWER_BOUND_KEYNUM; ++i) {
            newNode.mKeys[i] = btNode.mKeys[i + BTNode.MIN_DEGREE];
            btNode.mKeys[i + BTNode.MIN_DEGREE] = null;
        }

        // If the node is an internal node (not a leaf),
        // copy the its child pointers at the half right as well
        if (!btNode.mIsLeaf) {
            for (i = 0; i < BTNode.MIN_DEGREE; ++i) {
                newNode.mChildren[i] = btNode.mChildren[i + BTNode.MIN_DEGREE];
                btNode.mChildren[i + BTNode.MIN_DEGREE] = null;
            }
        }

        // The node at this point should have LOWER_BOUND_KEYNUM (aka min degree - 1) keys at this point.
        // We will move its right-most key to its parent node later.
        btNode.mCurrentKeyNum = BTNode.LOWER_BOUND_KEYNUM;

        // Do the right shift for relevant child pointers of the parent node
        // so that we can put the new node as its new child pointer
        for (i = parentNode.mCurrentKeyNum; i > nodeIdx; --i) {
            parentNode.mChildren[i + 1] = parentNode.mChildren[i];
            parentNode.mChildren[i] = null;
        }
        parentNode.mChildren[nodeIdx + 1] = newNode;

        // Do the right shift all the keys of the parent node the right side of the node index as well
        // so that we will have a slot for move a median key from the splitted node
        for (i = parentNode.mCurrentKeyNum - 1; i >= nodeIdx; --i) {
            parentNode.mKeys[i + 1] = parentNode.mKeys[i];
            parentNode.mKeys[i] = null;
        }
        parentNode.mKeys[nodeIdx] = btNode.mKeys[BTNode.LOWER_BOUND_KEYNUM];
        btNode.mKeys[BTNode.LOWER_BOUND_KEYNUM] = null;
        ++(parentNode.mCurrentKeyNum);
    }

    //
    // Find the predecessor node for a specified node
    //
    private BTNode<K, V> findPredecessor(BTNode<K, V> btNode, int nodeIdx) {
        if (btNode.mIsLeaf) {
            return btNode;
        }

        BTNode<K, V> predecessorNode;
        if (nodeIdx > -1) {
            predecessorNode = BTNode.getLeftChildAtIndex(btNode, nodeIdx);
            if (predecessorNode != null) {
                mIntermediateInternalNode = btNode;
                mNodeIdx = nodeIdx;
                btNode = findPredecessor(predecessorNode, -1);
            }

            return btNode;
        }

        predecessorNode = BTNode.getRightChildAtIndex(btNode, btNode.mCurrentKeyNum - 1);
        if (predecessorNode != null) {
            mIntermediateInternalNode = btNode;
            mNodeIdx = btNode.mCurrentKeyNum;
            btNode = findPredecessorForNode(predecessorNode, -1);
        }

        return btNode;
    }

    //
    // Find predecessor node of a specified node
    //
    private BTNode<K, V> findPredecessorForNode(BTNode<K, V> btNode, int keyIdx) {
        BTNode<K, V> predecessorNode;
        BTNode<K, V> originalNode = btNode;
        if (keyIdx > -1) {
            predecessorNode = BTNode.getLeftChildAtIndex(btNode, keyIdx);
            if (predecessorNode != null) {
                btNode = findPredecessorForNode(predecessorNode, -1);
                rebalanceTreeAtNode(originalNode, predecessorNode, keyIdx, REBALANCE_FOR_LEAF_NODE);
            }

            return btNode;
        }

        predecessorNode = BTNode.getRightChildAtIndex(btNode, btNode.mCurrentKeyNum - 1);
        if (predecessorNode != null) {
            btNode = findPredecessorForNode(predecessorNode, -1);
            rebalanceTreeAtNode(originalNode, predecessorNode, keyIdx, REBALANCE_FOR_LEAF_NODE);
        }

        return btNode;
    }

    //
    // Do the left rotation
    //
    private void performLeftRotation(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> rightSiblingNode) {
        int parentKeyIdx = nodeIdx;

        /*
        if (nodeIdx >= parentNode.mCurrentKeyNum) {
            // This shouldn't happen
            parentKeyIdx = nodeIdx - 1;
        }
         */
        // Move the parent key and relevant child to the deficient node
        btNode.mKeys[btNode.mCurrentKeyNum] = parentNode.mKeys[parentKeyIdx];
        btNode.mChildren[btNode.mCurrentKeyNum + 1] = rightSiblingNode.mChildren[0];
        ++(btNode.mCurrentKeyNum);

        // Move the leftmost key of the right sibling and relevant child pointer to the parent node
        parentNode.mKeys[parentKeyIdx] = rightSiblingNode.mKeys[0];
        --(rightSiblingNode.mCurrentKeyNum);
        // Shift all keys and children of the right sibling to its left
        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum; ++i) {
            rightSiblingNode.mKeys[i] = rightSiblingNode.mKeys[i + 1];
            rightSiblingNode.mChildren[i] = rightSiblingNode.mChildren[i + 1];
        }
        rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum] = rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum + 1];
        rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum + 1] = null;
    }

    //
    // Do the right rotation
    //
    private void performRightRotation(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> leftSiblingNode) {
        int parentKeyIdx = nodeIdx;
        if (nodeIdx >= parentNode.mCurrentKeyNum) {
            // This shouldn't happen
            parentKeyIdx = nodeIdx - 1;
        }

        // Shift all keys and children of the deficient node to the right
        // So that there will be available left slot for insertion
        btNode.mChildren[btNode.mCurrentKeyNum + 1] = btNode.mChildren[btNode.mCurrentKeyNum];
        for (int i = btNode.mCurrentKeyNum - 1; i >= 0; --i) {
            btNode.mKeys[i + 1] = btNode.mKeys[i];
            btNode.mChildren[i + 1] = btNode.mChildren[i];
        }

        // Move the parent key and relevant child to the deficient node
        btNode.mKeys[0] = parentNode.mKeys[parentKeyIdx];
        btNode.mChildren[0] = leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum];
        ++(btNode.mCurrentKeyNum);

        // Move the leftmost key of the right sibling and relevant child pointer to the parent node
        parentNode.mKeys[parentKeyIdx] = leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1];
        leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum] = null;
        --(leftSiblingNode.mCurrentKeyNum);
    }

    //
    // Do a left sibling merge
    // Return true if it should continue further
    // Return false if it is done
    //
    private boolean performMergeWithLeftSibling(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> leftSiblingNode) {
        if (nodeIdx == parentNode.mCurrentKeyNum) {
            // For the case that the node index can be the right most
            nodeIdx = nodeIdx - 1;
        }

        // Here we need to determine the parent node's index based on child node's index (nodeIdx)
        if (nodeIdx > 0) {
            if (leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1].mKey.compareTo(parentNode.mKeys[nodeIdx - 1].mKey) < 0) {
                nodeIdx = nodeIdx - 1;
            }
        }

        // Copy the parent key to the node (on the left)
        leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
        ++(leftSiblingNode.mCurrentKeyNum);

        // Copy keys and children of the node to the left sibling node
        for (int i = 0; i < btNode.mCurrentKeyNum; ++i) {
            leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum + i] = btNode.mKeys[i];
            leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum + i] = btNode.mChildren[i];
            btNode.mKeys[i] = null;
        }
        leftSiblingNode.mCurrentKeyNum += btNode.mCurrentKeyNum;
        leftSiblingNode.mChildren[leftSiblingNode.mCurrentKeyNum] = btNode.mChildren[btNode.mCurrentKeyNum];
        btNode.mCurrentKeyNum = 0;  // Abandon the node

        // Shift all relevant keys and children of the parent node to the left
        // since it lost one of its keys and children (by moving it to the child node)
        int i;
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        // Make sure the parent point to the correct child after the merge
        parentNode.mChildren[nodeIdx] = leftSiblingNode;

        if ((parentNode == mRoot) && (parentNode.mCurrentKeyNum == 0)) {
            // Root node is updated.  It should be done
            mRoot = leftSiblingNode;
            return false;
        }

        return true;
    }

    //
    // Do the right sibling merge
    // Return true if it should continue further
    // Return false if it is done
    //
    private boolean performMergeWithRightSibling(BTNode<K, V> btNode, int nodeIdx, BTNode<K, V> parentNode, BTNode<K, V> rightSiblingNode) {
        // Copy the parent key to right-most slot of the node
        btNode.mKeys[btNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
        ++(btNode.mCurrentKeyNum);

        // Copy keys and children of the right sibling to the node
        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum; ++i) {
            btNode.mKeys[btNode.mCurrentKeyNum + i] = rightSiblingNode.mKeys[i];
            btNode.mChildren[btNode.mCurrentKeyNum + i] = rightSiblingNode.mChildren[i];
        }
        btNode.mCurrentKeyNum += rightSiblingNode.mCurrentKeyNum;
        btNode.mChildren[btNode.mCurrentKeyNum] = rightSiblingNode.mChildren[rightSiblingNode.mCurrentKeyNum];
        rightSiblingNode.mCurrentKeyNum = 0;  // Abandon the sibling node

        // Shift all relevant keys and children of the parent node to the left
        // since it lost one of its keys and children (by moving it to the child node)
        int i;
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        // Make sure the parent point to the correct child after the merge
        parentNode.mChildren[nodeIdx] = btNode;

        if ((parentNode == mRoot) && (parentNode.mCurrentKeyNum == 0)) {
            // Root node is updated.  It should be done
            mRoot = btNode;
            return false;
        }

        return true;
    }

    //
    // Search the specified key within a node
    // Return index of the keys if it finds
    // Return -1 otherwise
    //
    private int searchKey(BTNode<K, V> btNode, K key) {
        for (int i = 0; i < btNode.mCurrentKeyNum; ++i) {
            if (key.compareTo(btNode.mKeys[i].mKey) == 0) {
                return i;
            } else if (key.compareTo(btNode.mKeys[i].mKey) < 0) {
                return -1;
            }
        }

        return -1;
    }

    //
    // List all the items in the tree
    //
    public void list(BTIteratorIF<K, V> iterImpl) {
        if (mSize < 1) {
            return;
        }
        if (iterImpl == null) {
            return;
        }
        BTNode<K, V> currentNode = mRoot;
        while (!currentNode.mIsLeaf) {
            currentNode = currentNode.mChildren[0];
        }
        while (currentNode != null) {
            for (int i = 0; i < currentNode.mCurrentKeyNum; ++i) {
                iterImpl.item(currentNode.mKeys[i].mKey, currentNode.mKeys[i].mValue);
            }
            currentNode = currentNode.mNext;
        }
    }

    //
    // Recursively loop to the tree and list out the keys and their values
    // Return true if it should continues listing out futher
    // Return false if it is done
    //
    private boolean listEntriesInOrder(BTNode<K, V> treeNode, BTIteratorIF<K, V> iterImpl) {
        if ((treeNode == null)
                || (treeNode.mCurrentKeyNum == 0)) {
            return false;
        }

        boolean bStatus;
        BTKeyValue<K, V> keyVal;
        int currentKeyNum = treeNode.mCurrentKeyNum;
        for (int i = 0; i < currentKeyNum; ++i) {
            listEntriesInOrder(BTNode.getLeftChildAtIndex(treeNode, i), iterImpl);

            keyVal = treeNode.mKeys[i];
            bStatus = iterImpl.item(keyVal.mKey, keyVal.mValue);
            if (!bStatus) {
                return false;
            }

            if (i == currentKeyNum - 1) {
                listEntriesInOrder(BTNode.getRightChildAtIndex(treeNode, i), iterImpl);
            }
        }

        return true;
    }

    //
    // Delete a key from the tree
    // Return value if it finds the key and delete it
    // Return null if it cannot find the key
    //
    
    public V delete(K key) {
        mIntermediateInternalNode = null;
        BTKeyValue<K, V> keyVal = deleteKey(null, mRoot, key, 0);
        if (keyVal == null) {
            return null;
        }
        --mSize;
        return keyVal.mValue;
    }
    /*
    public V delete(K key) {
    System.out.println("Eliminando clave: " + key);
    BTNode<K, V> currentNode = mRoot;
    while (currentNode != null) {
        System.out.println("Visitando nodo: " + currentNode + " | Número de claves: " + currentNode.mCurrentKeyNum);
        
        // Recorremos todas las claves del nodo actual para ver si alguna coincide con la clave a eliminar
        for (int i = 0; i < currentNode.mCurrentKeyNum; i++) {
            System.out.println("Comprobando clave: " + currentNode.mKeys[i].mKey);
            if (currentNode.mKeys[i].mKey.equals(key)) {
                System.out.println("Clave encontrada para eliminar: " + currentNode.mKeys[i].mKey);
                // Aquí realizarías la eliminación (por ejemplo, borrando la clave y ajustando el nodo)
                // Para esta versión simplificada retornamos el valor encontrado.
                return currentNode.mKeys[i].mValue;
            }
        }
        
        // Si la clave no se encontró en este nodo, continúa el recorrido usando la misma lógica que en search.
        // Para determinar la dirección, podemos usar la primera clave del nodo (o bien usar el índice 'i' como en search)
        BTKeyValue<K, V> currentKey = currentNode.mKeys[0];
        if (key.compareTo(currentKey.mKey) > 0) {
            System.out.println("Buscando a la derecha del nodo.");
            // Aquí usamos el método estático para obtener el hijo derecho.
            // Por ejemplo, usamos la posición (currentNode.mCurrentKeyNum - 1)
            currentNode = BTNode.getRightChildAtIndex(currentNode, currentNode.mCurrentKeyNum - 1);
        } else {
            System.out.println("Buscando a la izquierda del nodo.");
            // Y para el hijo izquierdo usamos la posición 0.
            currentNode = BTNode.getLeftChildAtIndex(currentNode, 0);
        }
    }

    return null;  // Si se recorre todo el árbol y no se encontró la clave
}
    */
    //
    // Delete a key from a tree node
    //
    private BTKeyValue<K, V> deleteKey(BTNode<K, V> parentNode, BTNode<K, V> btNode, K key, int nodeIdx) {
        int i;
        int nIdx;
        BTKeyValue<K, V> retVal;
        if (btNode == null) {
            // El árbol está vacío
            return null;
        }
        if (btNode.mIsLeaf) {
            nIdx = searchKey(btNode, key);
            if (nIdx < 0) {
                // No se encontró la clave
                println("Clave " + key + " no encontrada en el nodo hoja.");
                return null;
            }
            retVal = btNode.mKeys[nIdx];
            if ((btNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM) || (parentNode == null)) {
                // Eliminar la clave del nodo hoja
                for (i = nIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                    btNode.mKeys[i] = btNode.mKeys[i + 1];
                }
                btNode.mKeys[i] = null;
                --(btNode.mCurrentKeyNum);
                if (btNode.mCurrentKeyNum == 0) {
                    // El nodo hoja está vacío, eliminarlo
                    mRoot = null;
                }
                println("Clave " + key + " eliminada del nodo hoja.");
                return retVal;
            }

            // Find the left sibling
            BTNode<K, V> rightSibling;
            BTNode<K, V> leftSibling = BTNode.getLeftSiblingAtIndex(parentNode, nodeIdx);
            if ((leftSibling != null) && (leftSibling.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                // Remove the key and borrow a key from the left sibling
                moveLeftLeafSiblingKeyWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, leftSibling);
            } else {
                rightSibling = BTNode.getRightSiblingAtIndex(parentNode, nodeIdx);
                if ((rightSibling != null) && (rightSibling.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                    // Remove a key and borrow a key the right sibling
                    moveRightLeafSiblingKeyWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, rightSibling);
                } else {
                    // Merge to its sibling
                    boolean isRebalanceNeeded = false;
                    boolean bStatus;
                    if (leftSibling != null) {
                        // Merge with the left sibling
                        bStatus = doLeafSiblingMergeWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, leftSibling, false);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        } else if (parentNode.mCurrentKeyNum < BTNode.LOWER_BOUND_KEYNUM) {
                            // Need to rebalance the tree
                            isRebalanceNeeded = true;
                        }
                    } else {
                        // Merge with the right sibling
                        bStatus = doLeafSiblingMergeWithKeyRemoval(btNode, nodeIdx, nIdx, parentNode, rightSibling, true);
                        if (!bStatus) {
                            isRebalanceNeeded = false;
                        } else if (parentNode.mCurrentKeyNum < BTNode.LOWER_BOUND_KEYNUM) {
                            // Need to rebalance the tree
                            isRebalanceNeeded = true;
                        }
                    }

                    if (isRebalanceNeeded && (mRoot != null)) {
                        rebalanceTree(mRoot, parentNode, parentNode.mKeys[0].mKey);
                    }
                }
            }

            return retVal;  // Done with handling for the leaf node
        }

        //
        // At this point the node is an internal node
        //
        nIdx = searchKey(btNode, key);
        if (nIdx >= 0) {
            // We found the key in the internal node

            // Find its predecessor
            mIntermediateInternalNode = btNode;
            mNodeIdx = nIdx;
            BTNode<K, V> predecessorNode = findPredecessor(btNode, nIdx);
            BTKeyValue<K, V> predecessorKey = predecessorNode.mKeys[predecessorNode.mCurrentKeyNum - 1];

            // Swap the data of the deleted key and its predecessor (in the leaf node)
            BTKeyValue<K, V> deletedKey = btNode.mKeys[nIdx];
            btNode.mKeys[nIdx] = predecessorKey;
            predecessorNode.mKeys[predecessorNode.mCurrentKeyNum - 1] = deletedKey;

            // mIntermediateNode is done in findPrecessor
            return deleteKey(mIntermediateInternalNode, predecessorNode, deletedKey.mKey, mNodeIdx);
        }

        //
        // Find the child subtree (node) that contains the key
        //
        i = 0;
        BTKeyValue<K, V> currentKey = btNode.mKeys[0];
        while ((i < btNode.mCurrentKeyNum) && (key.compareTo(currentKey.mKey) > 0)) {
            ++i;
            if (i < btNode.mCurrentKeyNum) {
                currentKey = btNode.mKeys[i];
            } else {
                --i;
                break;
            }
        }

        BTNode<K, V> childNode;
        if (key.compareTo(currentKey.mKey) > 0) {
            childNode = BTNode.getRightChildAtIndex(btNode, i);
            if (childNode.mKeys[0].mKey.compareTo(btNode.mKeys[btNode.mCurrentKeyNum - 1].mKey) > 0) {
                // The right-most side of the node
                i = i + 1;
            }
        } else {
            childNode = BTNode.getLeftChildAtIndex(btNode, i);
        }

        return deleteKey(btNode, childNode, key, i);
    }

    //
    // Remove the specified key and move a key from the right leaf sibling to the node
    // Note: The node and its sibling must be leaves
    //
    private void moveRightLeafSiblingKeyWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> rightSiblingNode) {
        // Shift to the right where the key is deleted
        for (int i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
            btNode.mKeys[i] = btNode.mKeys[i + 1];
        }

        btNode.mKeys[btNode.mCurrentKeyNum - 1] = parentNode.mKeys[nodeIdx];
        parentNode.mKeys[nodeIdx] = rightSiblingNode.mKeys[0];

        for (int i = 0; i < rightSiblingNode.mCurrentKeyNum - 1; ++i) {
            rightSiblingNode.mKeys[i] = rightSiblingNode.mKeys[i + 1];
        }

        --(rightSiblingNode.mCurrentKeyNum);
    }

    //
    // Remove the specified key and move a key from the left leaf sibling to the node
    // Note: The node and its sibling must be leaves
    //
    private void moveLeftLeafSiblingKeyWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> leftSiblingNode) {
        // Use the parent key on the left side of the node
        nodeIdx = nodeIdx - 1;

        // Shift to the right to where the key will be deleted 
        for (int i = keyIdx; i > 0; --i) {
            btNode.mKeys[i] = btNode.mKeys[i - 1];
        }

        btNode.mKeys[0] = parentNode.mKeys[nodeIdx];
        parentNode.mKeys[nodeIdx] = leftSiblingNode.mKeys[leftSiblingNode.mCurrentKeyNum - 1];
        --(leftSiblingNode.mCurrentKeyNum);
    }

    //
    // Do the leaf sibling merge
    // Return true if we need to perform futher re-balancing action
    // Return false if we reach and update the root hence we don't need to go futher for re-balancing the tree
    //
    private boolean doLeafSiblingMergeWithKeyRemoval(BTNode<K, V> btNode,
            int nodeIdx,
            int keyIdx,
            BTNode<K, V> parentNode,
            BTNode<K, V> siblingNode,
            boolean isRightSibling) {
        int i;

        if (nodeIdx == parentNode.mCurrentKeyNum) {
            // Case node index can be the right most
            nodeIdx = nodeIdx - 1;
        }

        if (isRightSibling) {
            // Shift the remained keys of the node to the left to remove the key
            for (i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                btNode.mKeys[i] = btNode.mKeys[i + 1];
            }
            btNode.mKeys[i] = parentNode.mKeys[nodeIdx];
        } else {
            // Here we need to determine the parent node id based on child node id (nodeIdx)
            if (nodeIdx > 0) {
                if (siblingNode.mKeys[siblingNode.mCurrentKeyNum - 1].mKey.compareTo(parentNode.mKeys[nodeIdx - 1].mKey) < 0) {
                    nodeIdx = nodeIdx - 1;
                }
            }

            siblingNode.mKeys[siblingNode.mCurrentKeyNum] = parentNode.mKeys[nodeIdx];
            // siblingNode.mKeys[siblingNode.mCurrentKeyNum] = parentNode.mKeys[0];
            ++(siblingNode.mCurrentKeyNum);

            // Shift the remained keys of the node to the left to remove the key
            for (i = keyIdx; i < btNode.mCurrentKeyNum - 1; ++i) {
                btNode.mKeys[i] = btNode.mKeys[i + 1];
            }
            btNode.mKeys[i] = null;
            --(btNode.mCurrentKeyNum);
        }

        if (isRightSibling) {
            for (i = 0; i < siblingNode.mCurrentKeyNum; ++i) {
                btNode.mKeys[btNode.mCurrentKeyNum + i] = siblingNode.mKeys[i];
                siblingNode.mKeys[i] = null;
            }
            btNode.mCurrentKeyNum += siblingNode.mCurrentKeyNum;
        } else {
            for (i = 0; i < btNode.mCurrentKeyNum; ++i) {
                siblingNode.mKeys[siblingNode.mCurrentKeyNum + i] = btNode.mKeys[i];
                btNode.mKeys[i] = null;
            }
            siblingNode.mCurrentKeyNum += btNode.mCurrentKeyNum;
            btNode.mKeys[btNode.mCurrentKeyNum] = null;
        }

        // Shift the parent keys accordingly after the merge of child nodes
        for (i = nodeIdx; i < parentNode.mCurrentKeyNum - 1; ++i) {
            parentNode.mKeys[i] = parentNode.mKeys[i + 1];
            parentNode.mChildren[i + 1] = parentNode.mChildren[i + 2];
        }
        parentNode.mKeys[i] = null;
        parentNode.mChildren[parentNode.mCurrentKeyNum] = null;
        --(parentNode.mCurrentKeyNum);

        if (isRightSibling) {
            parentNode.mChildren[nodeIdx] = btNode;
        } else {
            parentNode.mChildren[nodeIdx] = siblingNode;
        }

        if ((mRoot == parentNode) && (mRoot.mCurrentKeyNum == 0)) {
            // Only root left
            mRoot = parentNode.mChildren[nodeIdx];
            mRoot.mIsLeaf = true;
            return false;  // Root has been changed, we don't need to go futher
        }

        return true;
    }

    //
    // Re-balance the tree at a specified node
    // Params:
    // parentNode = the parent node of the node needs to be re-balanced
    // btNode = the node needs to be re-balanced
    // nodeIdx = the index of the parent node's child array where the node belongs
    // balanceType = either REBALANCE_FOR_LEAF_NODE or REBALANCE_FOR_INTERNAL_NODE
    //   REBALANCE_FOR_LEAF_NODE: the node is a leaf
    //   REBALANCE_FOR_INTERNAL_NODE: the node is an internal node
    // Return:
    // true if it needs to continue rebalancing further
    // false if further rebalancing is no longer needed
    //
    private boolean rebalanceTreeAtNode(BTNode<K, V> parentNode, BTNode<K, V> btNode, int nodeIdx, int balanceType) {
        if (balanceType == REBALANCE_FOR_LEAF_NODE) {
            if ((btNode == null) || (btNode == mRoot)) {
                return false;
            }
        } else if (balanceType == REBALANCE_FOR_INTERNAL_NODE) {
            if (parentNode == null) {
                // Root node
                return false;
            }
        }

        if (btNode.mCurrentKeyNum >= BTNode.LOWER_BOUND_KEYNUM) {
            // The node doesn't need to rebalance
            return false;
        }

        BTNode<K, V> rightSiblingNode;
        BTNode<K, V> leftSiblingNode = BTNode.getLeftSiblingAtIndex(parentNode, nodeIdx);
        if ((leftSiblingNode != null) && (leftSiblingNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
            // Do right rotate
            performRightRotation(btNode, nodeIdx, parentNode, leftSiblingNode);
        } else {
            rightSiblingNode = BTNode.getRightSiblingAtIndex(parentNode, nodeIdx);
            if ((rightSiblingNode != null) && (rightSiblingNode.mCurrentKeyNum > BTNode.LOWER_BOUND_KEYNUM)) {
                // Do left rotate
                performLeftRotation(btNode, nodeIdx, parentNode, rightSiblingNode);
            } else {
                // Merge the node with one of the siblings
                boolean bStatus;
                if (leftSiblingNode != null) {
                    bStatus = performMergeWithLeftSibling(btNode, nodeIdx, parentNode, leftSiblingNode);
                } else {
                    bStatus = performMergeWithRightSibling(btNode, nodeIdx, parentNode, rightSiblingNode);
                }

                if (!bStatus) {
                    return false;
                }
            }
        }

        return true;
    }

    //
    // Re-balance the tree upward from the lower node to the upper node
    //
    private void rebalanceTree(BTNode<K, V> upperNode, BTNode<K, V> lowerNode, K key) {
        mStackTracer.clear();
        mStackTracer.add(new StackInfo(null, upperNode, 0));

        //
        // Find the child subtree (node) that contains the key
        //
        BTNode<K, V> parentNode, childNode;
        BTKeyValue<K, V> currentKey;
        int i;
        parentNode = upperNode;
        while ((parentNode != lowerNode) && !parentNode.mIsLeaf) {
            currentKey = parentNode.mKeys[0];
            i = 0;
            while ((i < parentNode.mCurrentKeyNum) && (key.compareTo(currentKey.mKey) > 0)) {
                ++i;
                if (i < parentNode.mCurrentKeyNum) {
                    currentKey = parentNode.mKeys[i];
                } else {
                    --i;
                    break;
                }
            }

            if (key.compareTo(currentKey.mKey) > 0) {
                childNode = BTNode.getRightChildAtIndex(parentNode, i);
                if (childNode.mKeys[0].mKey.compareTo(parentNode.mKeys[parentNode.mCurrentKeyNum - 1].mKey) > 0) {
                    // The right-most side of the node
                    i = i + 1;
                }
            } else {
                childNode = BTNode.getLeftChildAtIndex(parentNode, i);
            }

            if (childNode == null) {
                break;
            }

            if (key.compareTo(currentKey.mKey) == 0) {
                break;
            }

            mStackTracer.add(new StackInfo(parentNode, childNode, i));
            parentNode = childNode;
        }

        boolean bStatus;
        StackInfo stackInfo;
        while (!mStackTracer.isEmpty()) {
            stackInfo = mStackTracer.pop();
            if ((stackInfo != null) && !stackInfo.mNode.mIsLeaf) {
                bStatus = rebalanceTreeAtNode(stackInfo.mParent,
                        stackInfo.mNode,
                        stackInfo.mNodeIdx,
                        REBALANCE_FOR_INTERNAL_NODE);
                if (!bStatus) {
                    break;
                }
            }
        }
    }

    /**
     * Inner class StackInfo for tracing-back purpose Structure contains parent
     * node and node index
     */
    public class StackInfo {

        public BTNode<K, V> mParent = null;
        public BTNode<K, V> mNode = null;
        public int mNodeIdx = -1;

        public StackInfo(BTNode<K, V> parent, BTNode<K, V> node, int nodeIdx) {
            mParent = parent;
            mNode = node;
            mNodeIdx = nodeIdx;
        }
    }

    //splitleafNode
    private void splitLeafNode(BTNode<K, V> leafNode) {
        BTNode<K, V> newLeafNode = createNode();
        newLeafNode.mIsLeaf = true;

        // Calcular el punto medio correcto
        int mid = (BTNode.UPPER_BOUND_KEYNUM + 1) / 2;

        // Copiar la mitad derecha al nuevo nodo
        for (int i = mid; i < BTNode.UPPER_BOUND_KEYNUM; i++) {
            newLeafNode.mKeys[i - mid] = leafNode.mKeys[i];
            leafNode.mKeys[i] = null;
        }

        // Actualizar contadores de claves
        newLeafNode.mCurrentKeyNum = BTNode.UPPER_BOUND_KEYNUM - mid;
        leafNode.mCurrentKeyNum = mid;

        // Mantener la lista enlazada de nodos hoja
        newLeafNode.mNext = leafNode.mNext;
        leafNode.mNext = newLeafNode;

        // Insertar en el padre la primera clave del nuevo nodo
        insertKeyIntoParent(leafNode, newLeafNode.mKeys[0].mKey, newLeafNode);
    }

    private void insertKeyIntoParent(BTNode<K, V> leftChild, K key, BTNode<K, V> rightChild) {
        if (mRoot == leftChild) {
            // Si el nodo que se divide es la raíz, crear nueva raíz
            BTNode<K, V> newRoot = createNode();
            newRoot.mIsLeaf = false;
            newRoot.mKeys[0] = new BTKeyValue<>(key, null);
            newRoot.mChildren[0] = leftChild;
            newRoot.mChildren[1] = rightChild;
            newRoot.mCurrentKeyNum = 1;
            mRoot = newRoot;
            return;
        }
        // Encontrar el padre del nodo que se está dividiendo
        BTNode<K, V> parent = findParent(mRoot, leftChild);

        if (parent.mCurrentKeyNum < BTNode.UPPER_BOUND_KEYNUM) {
            // Si hay espacio en el padre, insertar directamente
            int insertPosition = findInsertPosition(parent, key);

            // Desplazar las claves y los hijos a la derecha
            for (int i = parent.mCurrentKeyNum; i > insertPosition; i--) {
                parent.mKeys[i] = parent.mKeys[i - 1];
                parent.mChildren[i + 1] = parent.mChildren[i];
            }

            // Insertar la nueva clave y el hijo derecho
            parent.mKeys[insertPosition] = new BTKeyValue<>(key, null);
            parent.mChildren[insertPosition + 1] = rightChild;
            parent.mCurrentKeyNum++;
        } else {
            // Si el padre está lleno, dividir el nodo padre
            splitInternalNode(parent, key, rightChild);
        }
    }

    private BTNode<K, V> findParent(BTNode<K, V> root, BTNode<K, V> child) {
        if (root == null || root.mIsLeaf) {
            return null;
        }

        // Buscar el hijo en los children del nodo actual
        for (int i = 0; i <= root.mCurrentKeyNum; i++) {
            if (root.mChildren[i] == child) {
                return root;
            }
        }

        // Buscar recursivamente en los hijos
        for (int i = 0; i <= root.mCurrentKeyNum; i++) {
            BTNode<K, V> result = findParent(root.mChildren[i], child);
            if (result != null) {
                return result;
            }
        }

        return null;
    }

    private int findInsertPosition(BTNode<K, V> node, K key) {
        int i = 0;
        while (i < node.mCurrentKeyNum && key.compareTo(node.mKeys[i].mKey) > 0) {
            i++;
        }
        return i;
    }

    private void splitInternalNode(BTNode<K, V> node, K newKey, BTNode<K, V> newChild) {
        // Crear un nuevo nodo interno
        BTNode<K, V> newNode = createNode();
        newNode.mIsLeaf = false;

        // Crear un array temporal para mantener todas las claves, incluyendo la nueva
        @SuppressWarnings("unchecked")
        BTKeyValue<K, V>[] tempKeys = new BTKeyValue[BTNode.UPPER_BOUND_KEYNUM + 1];
        BTNode<K, V>[] tempChildren = new BTNode[BTNode.UPPER_BOUND_KEYNUM + 2];

        // Copiar las claves y los hijos existentes
        for (int i = 0; i < node.mCurrentKeyNum; i++) {
            tempKeys[i] = node.mKeys[i];
            tempChildren[i] = node.mChildren[i];
        }
        tempChildren[node.mCurrentKeyNum] = node.mChildren[node.mCurrentKeyNum];

        // Encontrar la posición para la nueva clave
        int insertPos = findInsertPosition(node, newKey);

        // Desplazar elementos para hacer espacio
        for (int i = BTNode.UPPER_BOUND_KEYNUM; i > insertPos; i--) {
            tempKeys[i] = tempKeys[i - 1];
            tempChildren[i + 1] = tempChildren[i];
        }

        // Insertar la nueva clave y el hijo
        tempKeys[insertPos] = new BTKeyValue<>(newKey, null);
        tempChildren[insertPos + 1] = newChild;

        // Dividir los elementos entre los nodos
        int mid = BTNode.UPPER_BOUND_KEYNUM / 2;

        // Limpiar el nodo original
        node.mCurrentKeyNum = 0;

        // Copiar la primera mitad al nodo original
        for (int i = 0; i < mid; i++) {
            node.mKeys[i] = tempKeys[i];
            node.mChildren[i] = tempChildren[i];
            node.mCurrentKeyNum++;
        }
        node.mChildren[mid] = tempChildren[mid];

        // Copiar la segunda mitad al nuevo nodo
        int j = 0;
        for (int i = mid + 1; i <= BTNode.UPPER_BOUND_KEYNUM; i++) {
            newNode.mKeys[j] = tempKeys[i];
            newNode.mChildren[j] = tempChildren[i];
            newNode.mCurrentKeyNum++;
            j++;
        }
        newNode.mChildren[j] = tempChildren[BTNode.UPPER_BOUND_KEYNUM + 1];

        // Promover la clave media al padre
        K promotedKey = tempKeys[mid].mKey;
        insertKeyIntoParent(node, promotedKey, newNode);
    }
}
