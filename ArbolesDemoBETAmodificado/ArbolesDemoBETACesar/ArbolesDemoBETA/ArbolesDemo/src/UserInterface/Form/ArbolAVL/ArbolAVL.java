package UserInterface.Form.ArbolAVL;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import javax.swing.JTextArea;

import java.awt.Color;

public class ArbolAVL {
    private NodoAVL root;

    public ArbolAVL() {
        root = null;
    }

    // Obtener la raíz del árbol
    public NodoAVL getRoot() {
        return root;
    }

    // Método para insertar un nuevo nodo en el árbol AVL
    public boolean insert(int key) {
        if (buscar(key)) {
            return false; // El valor ya existe en el árbol
        }
        root = insertRecursivo(root, key);
        return true;
    }

    // Método recursivo para insertar un nodo
    private NodoAVL insertRecursivo(NodoAVL node, int key) {
        if (node == null) {
            return new NodoAVL(key);
        }

        if (key < node.getValor()) {
            node.setIzquierdo(insertRecursivo(node.getIzquierdo(), key));
        } else if (key > node.getValor()) {
            node.setDerecho(insertRecursivo(node.getDerecho(), key));
        } else {
            return node; // No se permiten duplicados
        }

        // Actualizar la altura del nodo actual
        node.setAltura(1 + Math.max(getAltura(node.getIzquierdo()), getAltura(node.getDerecho())));

        // Realizar el balanceo del árbol
        int balance = getBalance(node);

        // Casos de desbalanceo
        if (balance > 1 && key < node.getIzquierdo().getValor()) {
            return rotacionDerecha(node);
        }
        if (balance < -1 && key > node.getDerecho().getValor()) {
            return rotacionIzquierda(node);
        }
        if (balance > 1 && key > node.getIzquierdo().getValor()) {
            node.setIzquierdo(rotacionIzquierda(node.getIzquierdo()));
            return rotacionDerecha(node);
        }
        if (balance < -1 && key < node.getDerecho().getValor()) {
            node.setDerecho(rotacionDerecha(node.getDerecho()));
            return rotacionIzquierda(node);
        }

        return node;
    }

    // Obtener la altura de un nodo
    private int getAltura(NodoAVL node) {
        return node == null ? 0 : node.getAltura();
    }

    // Obtener el factor de equilibrio de un nodo
    private int getBalance(NodoAVL node) {
        return node == null ? 0 : getAltura(node.getIzquierdo()) - getAltura(node.getDerecho());
    }

    // Rotación simple a la derecha
    private NodoAVL rotacionDerecha(NodoAVL y) {
        NodoAVL x = y.getIzquierdo();
        NodoAVL T2 = x.getDerecho();

        x.setDerecho(y);
        y.setIzquierdo(T2);

        y.setAltura(Math.max(getAltura(y.getIzquierdo()), getAltura(y.getDerecho())) + 1);
        x.setAltura(Math.max(getAltura(x.getIzquierdo()), getAltura(x.getDerecho())) + 1);

        return x;
    }

    // Rotación simple a la izquierda
    private NodoAVL rotacionIzquierda(NodoAVL x) {
        NodoAVL y = x.getDerecho();
        NodoAVL T2 = y.getIzquierdo();

        y.setIzquierdo(x);
        x.setDerecho(T2);

        x.setAltura(Math.max(getAltura(x.getIzquierdo()), getAltura(x.getDerecho())) + 1);
        y.setAltura(Math.max(getAltura(y.getIzquierdo()), getAltura(y.getDerecho())) + 1);

        return y;
    }

    // Obtener la altura del árbol
    public int getAltura() {
        return getAltura(root);
    }

    // Método para dibujar el árbol
    public void drawTree(Graphics g, int x, int y, int xOffset, int yOffset) {
        drawNode(g, root, x, y, xOffset, yOffset);
    }

    // Método recursivo para dibujar un nodo y sus hijos
    private void drawNode(Graphics g, NodoAVL node, int x, int y, int xOffset, int yOffset) {
        if (node == null) return;
        g.setColor(Color.BLACK);
        g.fillOval(x - 15, y - 15, 30, 30);
        g.setColor(Color.WHITE);
        g.drawString(String.valueOf(node.getValor()), x - 5, y + 5);

        if (node.getIzquierdo() != null) {
            g.drawLine(x, y, x - xOffset, y + yOffset);
            drawNode(g, node.getIzquierdo(), x - xOffset, y + yOffset, xOffset / 2, yOffset);
        }
        if (node.getDerecho() != null) {
            g.drawLine(x, y, x + xOffset, y + yOffset);
            drawNode(g, node.getDerecho(), x + xOffset, y + yOffset, xOffset / 2, yOffset);
        }
    }

    // Método para buscar un valor en el árbol
    public boolean buscar(int value) {
        return buscarRecursivo(root, value);
    }

    // Método recursivo para buscar un valor
    private boolean buscarRecursivo(NodoAVL node, int value) {
        if (node == null) {
            return false; // El valor no se encuentra en el árbol
        }
        if (value == node.getValor()) {
            return true; // El valor se encontró
        } else if (value < node.getValor()) {
            return buscarRecursivo(node.getIzquierdo(), value); // Buscar en el subárbol izquierdo
        } else {
            return buscarRecursivo(node.getDerecho(), value); // Buscar en el subárbol derecho
        }
    }

    // Método para insertar un nodo con explicación detallada
    public boolean insertConExplicacion(int key, JTextArea consoleTextArea) {
        if (buscar(key)) {
            return false; // El valor ya existe en el árbol
        }
        root = insertRecursivoConExplicacion(root, key, consoleTextArea);
        return true;
    }

    private NodoAVL insertRecursivoConExplicacion(NodoAVL node, int key, JTextArea consoleTextArea) {
        if (node == null) {
            consoleTextArea.append("Nodo nulo alcanzado. Insertando " + key + " aquí.\n");
            return new NodoAVL(key);
        }

        consoleTextArea.append("Visitando nodo: " + node.getValor() + "\n");
        if (key < node.getValor()) {
            consoleTextArea.append(key + " es menor que " + node.getValor() + ". Moviéndose al subárbol izquierdo.\n");
            node.setIzquierdo(insertRecursivoConExplicacion(node.getIzquierdo(), key, consoleTextArea));
        } else if (key > node.getValor()) {
            consoleTextArea.append(key + " es mayor que " + node.getValor() + ". Moviéndose al subárbol derecho.\n");
            node.setDerecho(insertRecursivoConExplicacion(node.getDerecho(), key, consoleTextArea));
        } else {
            return node; // No se permiten duplicados
        }

        // Actualizar la altura del nodo actual
        node.setAltura(1 + Math.max(getAltura(node.getIzquierdo()), getAltura(node.getDerecho())));

        // Realizar el balanceo del árbol
        int balance = getBalance(node);

        // Casos de desbalanceo
        if (balance > 1 && key < node.getIzquierdo().getValor()) {
            consoleTextArea.append("Rotación derecha en el nodo " + node.getValor() + ".\n");
            return rotacionDerecha(node);
        }
        if (balance < -1 && key > node.getDerecho().getValor()) {
            consoleTextArea.append("Rotación izquierda en el nodo " + node.getValor() + ".\n");
            return rotacionIzquierda(node);
        }
        if (balance > 1 && key > node.getIzquierdo().getValor()) {
            consoleTextArea.append("Rotación izquierda-derecha en el nodo " + node.getValor() + ".\n");
            node.setIzquierdo(rotacionIzquierda(node.getIzquierdo()));
            return rotacionDerecha(node);
        }
        if (balance < -1 && key < node.getDerecho().getValor()) {
            consoleTextArea.append("Rotación derecha-izquierda en el nodo " + node.getValor() + ".\n");
            node.setDerecho(rotacionDerecha(node.getDerecho()));
            return rotacionIzquierda(node);
        }

        return node;
    }
    
    
    // Método para encontrar el nodo con el valor mínimo en el subárbol
    private NodoAVL minValueNode(NodoAVL node) {
        NodoAVL current = node;
        while (current.getIzquierdo() != null) {
            current = current.getIzquierdo();
        }
        return current;
    }

    // Método para eliminar un nodo con explicación detallada
    public boolean eliminarConExplicacion(int key, JTextArea consoleTextArea) {
        if (!buscar(key)) {
            return false; // El valor no existe en el árbol
        }
        root = eliminarRecursivoConExplicacion(root, key, consoleTextArea);
        return true;
    }

    private NodoAVL eliminarRecursivoConExplicacion(NodoAVL node, int key, JTextArea consoleTextArea) {
        if (node == null) {
            return node;
        }

        consoleTextArea.append("Visitando nodo: " + node.getValor() + "\n");
        if (key < node.getValor()) {
            consoleTextArea.append(key + " es menor que " + node.getValor() + ". Moviéndose al subárbol izquierdo.\n");
            node.setIzquierdo(eliminarRecursivoConExplicacion(node.getIzquierdo(), key, consoleTextArea));
        } else if (key > node.getValor()) {
            consoleTextArea.append(key + " es mayor que " + node.getValor() + ". Moviéndose al subárbol derecho.\n");
            node.setDerecho(eliminarRecursivoConExplicacion(node.getDerecho(), key, consoleTextArea));
        } else {
            consoleTextArea.append("Nodo " + key + " encontrado. Procediendo a eliminar.\n");
            if (node.getIzquierdo() == null || node.getDerecho() == null) {
                NodoAVL temp = (node.getIzquierdo() != null) ? node.getIzquierdo() : node.getDerecho();
                if (temp == null) {
                    consoleTextArea.append("Nodo " + key + " es una hoja. Eliminando.\n");
                    node = null;
                } else {
                    consoleTextArea.append("Nodo " + key + " tiene un solo hijo. Reemplazando.\n");
                    node = temp;
                }
            } else {
                consoleTextArea.append("Nodo " + key + " tiene dos hijos. Buscando sucesor.\n");
                NodoAVL temp = minValueNode(node.getDerecho());
                node.setValor(temp.getValor());
                node.setDerecho(eliminarRecursivoConExplicacion(node.getDerecho(), temp.getValor(), consoleTextArea));
            }
        }

        if (node == null) {
            return node;
        }

        // Actualizar la altura del nodo actual
        node.setAltura(1 + Math.max(getAltura(node.getIzquierdo()), getAltura(node.getDerecho())));

        // Realizar el balanceo del árbol
        int balance = getBalance(node);

        // Casos de desbalanceo
        if (balance > 1 && getBalance(node.getIzquierdo()) >= 0) {
            consoleTextArea.append("Rotación derecha en el nodo " + node.getValor() + ".\n");
            return rotacionDerecha(node);
        }
        if (balance > 1 && getBalance(node.getIzquierdo()) < 0) {
            consoleTextArea.append("Rotación izquierda-derecha en el nodo " + node.getValor() + ".\n");
            node.setIzquierdo(rotacionIzquierda(node.getIzquierdo()));
            return rotacionDerecha(node);
        }
        if (balance < -1 && getBalance(node.getDerecho()) <= 0) {
            consoleTextArea.append("Rotación izquierda en el nodo " + node.getValor() + ".\n");
            return rotacionIzquierda(node);
        }
        if (balance < -1 && getBalance(node.getDerecho()) > 0) {
            consoleTextArea.append("Rotación derecha-izquierda en el nodo " + node.getValor() + ".\n");
            node.setDerecho(rotacionDerecha(node.getDerecho()));
            return rotacionIzquierda(node);
        }

        return node;
    }

    // Método para generar la matriz de adyacencia basada en los nodos existentes
    public int[][] generarMatrizAdyacencia() {
        List<Integer> nodos = new ArrayList<>();
        recolectarNodos(root, nodos); // Recolectar todos los nodos del árbol

        int n = nodos.size();
        int[][] matriz = new int[n][n];

        // Llenar la matriz de adyacencia
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matriz[i][j] = 0; // Inicializar la matriz con 0
            }
        }

        // Recorrer el árbol y llenar la matriz
        for (int i = 0; i < n; i++) {
            NodoAVL node = buscarNodo(root, nodos.get(i));
            if (node != null) {
                if (node.getIzquierdo() != null) {
                    int indiceIzquierdo = nodos.indexOf(node.getIzquierdo().getValor());
                    matriz[i][indiceIzquierdo] = 1;
                }
                if (node.getDerecho() != null) {
                    int indiceDerecho = nodos.indexOf(node.getDerecho().getValor());
                    matriz[i][indiceDerecho] = 1;
                }
            }
        }

        return matriz;
    }

    // Método para recolectar todos los nodos del árbol
    private void recolectarNodos(NodoAVL node, List<Integer> nodos) {
        if (node != null) {
            nodos.add(node.getValor());
            recolectarNodos(node.getIzquierdo(), nodos);
            recolectarNodos(node.getDerecho(), nodos);
        }
    }

    // Método para buscar un nodo por su valor
    private NodoAVL buscarNodo(NodoAVL node, int valor) {
        if (node == null || node.getValor() == valor) {
            return node;
        }
        if (valor < node.getValor()) {
            return buscarNodo(node.getIzquierdo(), valor);
        } else {
            return buscarNodo(node.getDerecho(), valor);
        }
    }

    // Método para realizar el recorrido inorden con explicación
    public void recorridoInordenConExplicacion(JTextArea consoleTextArea) {
        consoleTextArea.append("Recorrido inorden:\n");
    
        StringBuilder resultado = new StringBuilder(); // Para almacenar el recorrido final
        inordenRecursivoConExplicacion(root, consoleTextArea, resultado);
    
        // Imprimir el recorrido final al terminar la recursión
        consoleTextArea.append("Recorrido final: " + resultado.toString().trim() + "\n");
    }
    
    private void inordenRecursivoConExplicacion(NodoAVL node, JTextArea consoleTextArea, StringBuilder resultado) {
        if (node != null) {
            consoleTextArea.append("Visitando subárbol izquierdo de " + node.getValor() + "\n");
            inordenRecursivoConExplicacion(node.getIzquierdo(), consoleTextArea, resultado);
    
            consoleTextArea.append("Procesando nodo: " + node.getValor() + "\n");
            resultado.append(node.getValor()).append(" "); // Agregar el valor al recorrido final
    
            consoleTextArea.append("Visitando subárbol derecho de " + node.getValor() + "\n");
            inordenRecursivoConExplicacion(node.getDerecho(), consoleTextArea, resultado);
        } else {
            consoleTextArea.append("Nodo nulo alcanzado. Retrocediendo.\n");
        }
    }
    

    // Método para realizar el recorrido preorden con explicación
    public void recorridoPreordenConExplicacion(JTextArea consoleTextArea) {
        consoleTextArea.append("Recorrido preorden:\n");
    
        StringBuilder resultado = new StringBuilder(); // Para almacenar el recorrido final
        preordenRecursivoConExplicacion(root, consoleTextArea, resultado);
    
        // Imprimir el recorrido final al terminar la recursión
        consoleTextArea.append("Recorrido final: " + resultado.toString().trim() + "\n");
    }
    
    private void preordenRecursivoConExplicacion(NodoAVL node, JTextArea consoleTextArea, StringBuilder resultado) {
        if (node != null) {
            consoleTextArea.append("Procesando nodo: " + node.getValor() + "\n");
            resultado.append(node.getValor()).append(" "); // Agregar el valor al recorrido final
    
            consoleTextArea.append("Visitando subárbol izquierdo de " + node.getValor() + "\n");
            preordenRecursivoConExplicacion(node.getIzquierdo(), consoleTextArea, resultado);
    
            consoleTextArea.append("Visitando subárbol derecho de " + node.getValor() + "\n");
            preordenRecursivoConExplicacion(node.getDerecho(), consoleTextArea, resultado);
        } else {
            consoleTextArea.append("Nodo nulo alcanzado. Retrocediendo.\n");
        }
    }
    

    // Método para realizar el recorrido postorden con explicación
    public void recorridoPostordenConExplicacion(JTextArea consoleTextArea) {
        consoleTextArea.append("Recorrido postorden:\n");
    
        StringBuilder resultado = new StringBuilder(); // Para almacenar el recorrido final
        postordenRecursivoConExplicacion(root, consoleTextArea, resultado);
    
        // Imprimir el recorrido final al terminar la recursión
        consoleTextArea.append("Recorrido final: " + resultado.toString().trim() + "\n");
    }
    
    private void postordenRecursivoConExplicacion(NodoAVL node, JTextArea consoleTextArea, StringBuilder resultado) {
        if (node != null) {
            consoleTextArea.append("Visitando subárbol izquierdo de " + node.getValor() + "\n");
            postordenRecursivoConExplicacion(node.getIzquierdo(), consoleTextArea, resultado);
    
            consoleTextArea.append("Visitando subárbol derecho de " + node.getValor() + "\n");
            postordenRecursivoConExplicacion(node.getDerecho(), consoleTextArea, resultado);
    
            consoleTextArea.append("Procesando nodo: " + node.getValor() + "\n");
            resultado.append(node.getValor()).append(" "); // Agregar el valor al recorrido final
        } else {
            consoleTextArea.append("Nodo nulo alcanzado. Retrocediendo.\n");
        }
    }
    

    //Metodo para realizar el recorrido level order con explicacion
    public void recorridoLevelOrderConExplicacion(JTextArea consoleTextArea) {
        consoleTextArea.append("Recorrido level order:\n");
        if (root == null) {
            consoleTextArea.append("El árbol está vacío.\n");
            return;
        }
    
        Queue<NodoAVL> cola = new LinkedList<>();
        cola.add(root);
        
        StringBuilder resultado = new StringBuilder(); // Para almacenar el recorrido final
    
        while (!cola.isEmpty()) {
            NodoAVL node = cola.poll();
            consoleTextArea.append("Procesando nodo: " + node.getValor() + "\n");
            resultado.append(node.getValor()).append(" "); // Agregar el valor al recorrido final
    
            if (node.getIzquierdo() != null) {
                consoleTextArea.append("Agregando hijo izquierdo de " + node.getValor() + " a la cola.\n");
                cola.add(node.getIzquierdo());
            }
            if (node.getDerecho() != null) {
                consoleTextArea.append("Agregando hijo derecho de " + node.getValor() + " a la cola.\n");
                cola.add(node.getDerecho());
            }
        }
    
        // Imprimir el recorrido final al terminar el bucle
        consoleTextArea.append("Recorrido final: " + resultado.toString().trim() + "\n");
    }
}
