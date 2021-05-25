package ProductorCola;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.jms.Connection;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.Topic;

import org.apache.activemq.ActiveMQConnectionFactory;

public class ProductorCola {

    public static void main(String [] args) throws Exception {
        thread(new Productor(), false);
        Thread.sleep(1000);
    }

    public static void thread(Runnable runnable, boolean daemon) {
        Thread brokerThread = new Thread(runnable);
        brokerThread.setDaemon(daemon);
        brokerThread.start();
    }

    public static class Productor implements Runnable {
        public void run() {
            try {
                //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.9.166:61617");
            	ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.9.103:61617");
                //ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://172.16.2.190:61617");
                
                connectionFactory.setUserName("admin");
                //connectionFactory.setPassword("2019_Esb_PRD_JB0ss");
                connectionFactory.setPassword("redhat");

                Connection connection = connectionFactory.createConnection();
                connection.start();

                Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

                //Destination destination = session.createQueue("prueba");
                Topic destination = session.createTopic("ODS_Agentes_activos");

                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
                //producer.setTimeToLive(5000);

                DateFormat formato = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
                String texto = "Hola Mundo: " + formato.format(new Date());
                TextMessage mensaje = session.createTextMessage(texto);

                producer.send(mensaje);
                System.out.println("Mensaje enviado: " + mensaje.getText());

                session.close();
                connection.close();
            }
            catch (Exception e) {
                System.out.println("Excepcion: " + e);
                e.printStackTrace();
            }
        }
    }

        public synchronized void onException(JMSException ex) {
        	System.out.println("Ocurrio una excepcion...");
        }
}

