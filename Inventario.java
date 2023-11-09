import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class Inventario {
    private Nodo raiz;
    private LinkedList<Producto> listaProductos;

    public Inventario() {
        raiz = null;
        listaProductos = new LinkedList<>();
    }

    public List<Producto> obtenerListaProductos() {
        return listaProductos;
    }

    private int altura(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return nodo.altura;
    }

    private int obtenerEquilibrio(Nodo nodo) {
        if (nodo == null) {
            return 0;
        }
        return altura(nodo.izquierdo) - altura(nodo.derecho);
    }

    // Rotación a la derecha
    private Nodo rotarDerecha(Nodo y) {
        Nodo x = y.izquierdo;
        Nodo T2 = x.derecho;
        x.derecho = y;
        y.izquierdo = T2;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        return x;
    }

    // Rotación a la izquierda
    private Nodo rotarIzquierda(Nodo x) {
        Nodo y = x.derecho;
        Nodo T2 = y.izquierdo;
        y.izquierdo = x;
        x.derecho = T2;
        x.altura = Math.max(altura(x.izquierdo), altura(x.derecho)) + 1;
        y.altura = Math.max(altura(y.izquierdo), altura(y.derecho)) + 1;
        return y;
    }

    // Método para insertar un producto en el árbol
    public void insertar(Producto producto) {
        raiz = insertarRec(raiz, producto); // Llama al método recursivo insertarRec con la raíz del árbol y el producto a insertar
    }

    // Método recursivo para insertar un producto en el árbol
    private Nodo insertarRec(Nodo nodo, Producto producto) {
        if (nodo == null) {
            return new Nodo(producto); // Crea un nuevo nodo con el producto si el nodo actual es nulo
        }
        if (producto.nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = insertarRec(nodo.izquierdo, producto); // Inserta en el subárbol izquierdo si el nombre es menor
        } else if (producto.nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = insertarRec(nodo.derecho, producto); // Inserta en el subárbol derecho si el nombre es mayor
        } else {
            // Si el producto ya existe, actualiza la cantidad
            nodo.producto.cantidad += producto.cantidad;
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho)); // Actualiza la altura del nodo
        int equilibrio = obtenerEquilibrio(nodo); // Calcula el equilibrio del nodo
        // Casos de rotación para restaurar el equilibrio
        if (equilibrio > 1) {
            if (producto.nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo); // Rotación a la derecha
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo); // Rotación a la derecha después de una rotación a la izquierda
            }
        }
        if (equilibrio < -1) {
            if (producto.nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo); // Rotación a la izquierda
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo); // Rotación a la izquierda después de una rotación a la derecha
            }
        }
        return nodo; // Devuelve el nodo actual, que puede haber cambiado debido a las operaciones de inserción y rotación
    }

    // Método para eliminar un producto del árbol
    public void eliminar(String nombre) throws Exception {
        Scanner var = new Scanner(System.in);
        System.out.print( "\u001B[94m" + "Ingrese la cantidad a eliminar: "+ "\u001B[0m");
        int cantidadEliminar = var.nextInt();
        raiz = eliminarRec(raiz, nombre, cantidadEliminar); // Llama al método recursivo eliminarRec con la raíz del árbol
    }

    // Método recursivo para eliminar un producto del árbol
    private Nodo eliminarRec(Nodo nodo, String nombre, int cantidadEliminar) throws Exception {
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, nombre, cantidadEliminar); // Busca en el subárbol izquierdo
        } else if (nombre.compareTo(nodo.producto.nombre) > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, nombre, cantidadEliminar); // Busca en el subárbol derecho
        } else {
            // Si el producto existe, se descuenta la cantidad
            nodo.producto.cantidad -= cantidadEliminar;
            if (nodo.producto.cantidad == 0) {
                nodo.producto.cantidad -= cantidadEliminar;
                // Si la cantidad llega a cero, se elimina el nodo
                if (nodo.izquierdo == null) {
                    return nodo.derecho; // Reemplaza el nodo por su hijo derecho
                } else if (nodo.derecho == null) {
                    return nodo.izquierdo; // Reemplaza el nodo por su hijo izquier mdo
                }
                Nodo sucesor = encontrarSucesor(nodo.derecho); // Encuentra el sucesor en el subárbol derecho
                nodo.producto = sucesor.producto; // Copia el sucesor al nodo actual
                nodo.derecho = eliminarRec(nodo.derecho, sucesor.producto.nombre, cantidadEliminar); // Elimina el
                                                                                                     // sucesor
            } else if (nodo.producto.cantidad < 0) {
                System.out.println( "\u001B[91m"+
                        "La cantidad ingresada es mayor a la que se encuentra disponible en stock." + "\u001B[0m" + "\nLa cantidad disponible de "
                                + nodo.producto.nombre + " es " + nodo.producto.cantidad );
            }
        }
        nodo.altura = 1 + Math.max(altura(nodo.izquierdo), altura(nodo.derecho));
        int equilibrio = obtenerEquilibrio(nodo); // Calcula el equilibrio del nodo
        // Casos de rotación para restaurar el equilibrio
        if (equilibrio > 1) {
            if (nombre.compareTo(nodo.izquierdo.producto.nombre) < 0) {
                return rotarDerecha(nodo); // Rotación a la derecha
            } else {
                nodo.izquierdo = rotarIzquierda(nodo.izquierdo);
                return rotarDerecha(nodo); // Rotación a la derecha después de una rotación a la izquierda
            }
        }
        if (equilibrio < -1) {
            if (nombre.compareTo(nodo.derecho.producto.nombre) > 0) {
                return rotarIzquierda(nodo); // Rotación a la izquierda
            } else {
                nodo.derecho = rotarDerecha(nodo.derecho);
                return rotarIzquierda(nodo); // Rotación a la izquierda después de una rotación a la derecha
            }
        }
        return nodo; // Devuelve el nodo actual, que puede haber cambiado debido a las operaciones de eliminación y rotación
    }

    // Método para encontrar el sucesor de un nodo
    private Nodo encontrarSucesor(Nodo nodo) {
        Nodo actual = nodo;
        while (actual.izquierdo != null) {
            actual = actual.izquierdo;
        }
        return actual;
    }

    // Método para mostrar los productos en orden alfabético
    public void mostrarInorden() {
        listaProductos.clear(); // Limpia la lista antes de comenzar
        mostrarInordenRec(raiz);
    }

    // Método recursivo para recorrer el árbol en orden y almacenar los productos en la lista
    private void mostrarInordenRec(Nodo nodo) {
        if (nodo != null) {
            mostrarInordenRec(nodo.izquierdo);
            listaProductos.add(nodo.producto);
            mostrarInordenRec(nodo.derecho);
        }
    }

    // Método para buscar un producto en el árbol
    public Producto buscar(String nombre) throws Exception {
        return buscarRec(raiz, nombre); // Llama al método recursivo buscarRec con la raíz del árbol y el nombre del producto a buscar
    }

    // Método recursivo para buscar un producto en el árbol
    private Producto buscarRec(Nodo nodo, String nombre) throws Exception {
        if (nodo == null) {
            throw new Exception("\u001B[91m" + "El producto no se encuentra en el árbol."+ "\u001B[0m");
        }
        if (nombre.equals(nodo.producto.nombre)) {
            System.out
                    .println("\u001B[92m"+ "Producto encontrado: " + nodo.producto.nombre + ", Cantidad: " + nodo.producto.cantidad + "\u001B[0m");
            return nodo.producto; // El producto se ha encontrado en el nodo actual
        }
        if (nombre.compareTo(nodo.producto.nombre) < 0) {
            return buscarRec(nodo.izquierdo, nombre); // Busca en el subárbol izquierdo si el nombre es menor
        }
        return buscarRec(nodo.derecho, nombre); // Busca en el subárbol derecho si el nombre es mayor
    }

    public boolean esArbolBalanceado(Nodo nodo) {
        if (nodo == null) {
            return true; // Un árbol vacío se considera balanceado
        }
    
        int alturaIzquierdo = altura(nodo.izquierdo); // Calcula la altura del subárbol izquierdo
        int alturaDerecho = altura(nodo.derecho); // Calcula la altura del subárbol derecho
    
        // Verificar si la diferencia de altura es mayor que 1
        if (Math.abs(alturaIzquierdo - alturaDerecho) > 1) {
            return false; // Si la diferencia es mayor que 1, el árbol no es balanceado
        }
    
        // Verificar la condición de equilibrio para los subárboles izquierdo y derecho
        return esArbolBalanceado(nodo.izquierdo) && esArbolBalanceado(nodo.derecho);
    }
    
    public boolean esArbolBalanceado() {
        return esArbolBalanceado(raiz); // Llama a la función para verificar si todo el árbol es balanceado
    }
    
    public void imprimirArbol(Nodo nodo, String prefijo, boolean esIzquierdo) {
        if (nodo != null) {
            // Imprime el nombre del producto en el nodo con formato jerárquico
            System.out.println(prefijo + (esIzquierdo ? "├── " : "└── ") + nodo.producto.nombre);
    
            // Llama recursivamente para imprimir los subárboles izquierdo y derecho con prefijo adecuado
            imprimirArbol(nodo.izquierdo, prefijo + (esIzquierdo ? "│   " : "    "), true);
            imprimirArbol(nodo.derecho, prefijo + (esIzquierdo ? "│   " : "    "), false);
        }
    }

    public void imprimirArbol() {
        imprimirArbol(raiz, "", true);
    }
}