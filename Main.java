import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        final String AZUL = "\u001B[94m";
        final String ROJO = "\u001B[91m";
        final String VERDE = "\u001B[92m";
        final String RESET = "\u001B[0m";

        Inventario arbol = new Inventario();
        Scanner var = new Scanner(System.in);
        int opcion;

        do {
            // Mostrar opciones al usuario
            System.out.println("\n1- Agregar Producto.");
            System.out.println("2- Eliminar Producto.");
            System.out.println("3- Buscar Producto.");
            System.out.println("4- Mostrar Inventario.");
            System.out.println(VERDE + "---------EXTRAS-------------" + RESET);
            System.out.println("5- Verificar si el árbol es balanceado.");
            System.out.println("6- Imprimir Arbol.");
            System.out.println("\n0- Salir.");

            try {
                System.out.print(AZUL + "\nIngrese la opción: " + RESET);
                opcion = var.nextInt();
                var.nextLine(); // Consumir el salto de línea pendiente

                switch (opcion) {
                    case 1:
                        System.out.print(AZUL + "Ingrese el nombre del producto: " + RESET);
                        String nombre = var.nextLine();
                        System.out.print(AZUL + "Ingrese la cantidad a ingresar: " + RESET);
                        int cantidad = var.nextInt();
                        arbol.insertar(new Producto(nombre, cantidad));
                        break;
                        case 2:
                        try {
                            System.out.print(AZUL + "Ingrese el nombre del producto a eliminar: " + RESET);
                            String nombreEliminar = var.nextLine();
                            arbol.eliminar(nombreEliminar);
                        } catch (Exception e) {
                            System.out.println(ROJO + "ERROR: " + e.getMessage() + RESET);
                        }
                        break;
                    case 3:
                        try {
                            System.out.print(AZUL + "Ingrese el nombre del producto a buscar: "+ RESET);
                            String nombreBuscar = var.nextLine();
                            arbol.buscar(nombreBuscar);
                        } catch (Exception e) {
                            System.out.println(ROJO + "ERROR: NO EXISTE EL PRODUCTO EN EL INVENTARIO: " + e.getMessage()+ RESET);
                        }
                        break;
                    case 4:
                        arbol.mostrarInorden();
                        System.out.println("\nLista de productos en orden alfabético:");
                        for (Producto producto : arbol.obtenerListaProductos()) {
                            System.out.println("Producto: " + producto.nombre + ", Cantidad: " + producto.cantidad);
                        }
                        break;
                    case 5:
                        boolean esBalanceado = arbol.esArbolBalanceado();
                        System.out.println("El árbol está balanceado: " + esBalanceado);
                        break;
                    case 6:
                        arbol.imprimirArbol();
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println(ROJO + "ERROR: Debe ingresar un número entero para seleccionar una opción." + RESET);
                var.nextLine(); // Consumir la entrada inválida
                opcion = -1; // Establecer una opción inválida para continuar el bucle
            }

        } while (opcion != 0);

        // Cerrar el Scanner al final del programa
        var.close();
    }
}
