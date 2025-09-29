package projectmagenta.app;

import projectmagenta.dao.DBConnection;
import projectmagenta.view.MainFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

/**
 * Sistema de Gestión de Películas - Cines Magenta
 * Clase principal que inicia la aplicación, configura el Look and Feel,
 * y verifica la conexión a la base de datos antes de mostrar la interfaz gráfica principal. 
 *
 * @author arekk
 */
public class Main {

    /**
     * Método principal que inicia la aplicación.
     * Configura la salida estándar en UTF-8, establece el Look and Feel,
     * verifica la conexión a la base de datos y lanza la ventana principal si todo es correcto.
     * @param args argumentos de línea de comandos
     * @throws java.io.UnsupportedEncodingException si la codificación no es soportada
     */
    public static void main(String[] args) throws java.io.UnsupportedEncodingException {
        System.setOut(new java.io.PrintStream(System.out, true, "UTF-8"));
        System.setErr(new java.io.PrintStream(System.err, true, "UTF-8"));
        // Configurar el Look and Feel antes de crear la interfaz
        configureLookAndFeel();
        
        // Verificar conexión a la base de datos con manejo de excepciones
        try {
            if (testDatabaseConnection()) {
                // Iniciar la interfaz gráfica en el EDT (Event Dispatch Thread)
                SwingUtilities.invokeLater(() -> {
                    try {
                        MainFrame mainFrame = new MainFrame();
                        mainFrame.setVisible(true);
                    } catch (Exception e) {
                        System.err.println("Error al inicializar la interfaz: " + e.getMessage());
                        e.printStackTrace();
                    }
                });
            } else {
                System.err.println("No se pudo iniciar la aplicación debido a problemas de conexión a la base de datos.");
                System.exit(1);
            }
        } catch (Exception ex) {
            System.err.println("Ocurrió un error inesperado al verificar la conexión a la base de datos.\nDetalles técnicos: " + ex.getMessage());
            System.exit(2);
        }
    }
    
    /**
     * Configura el Look and Feel de la aplicación para una apariencia moderna.
     * Intenta usar Nimbus si está disponible, de lo contrario usa el predeterminado.
     */
    private static void configureLookAndFeel() {
        try {
            // Intentar usar Nimbus Look and Feel
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    return;
                }
            }
            // Si Nimbus no está disponible, usar el del sistema por defecto
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.out.println("No se pudo configurar el Look and Feel: " + e.getMessage());
            // Continuar con el Look and Feel por defecto
        }
    }
    
    /**
     * Verifica la conexión a la base de datos antes de iniciar la aplicación.
     * Muestra mensajes de error detallados si la conexión falla.
     * @return true si la conexión es exitosa, false en caso contrario
     */
    private static boolean testDatabaseConnection() {
        System.out.println("Verificando conexión a la base de datos...");
        boolean connected = DBConnection.testConnection();
        
        if (connected) {
            System.out.println("[SUCCESS] Conexion a la base de datos exitosa");
        } else {
            System.err.println("[ERROR] No se pudo conectar a la base de datos");
            System.err.println("Verifique que:");
            System.err.println("- MySQL esté ejecutándose");
            System.err.println("- La base de datos 'Cine_DB' exista");
            System.err.println("- Las credenciales sean correctas");
            System.err.println("- El driver MySQL esté en el classpath");
        }
        
        return connected;
    }
}
