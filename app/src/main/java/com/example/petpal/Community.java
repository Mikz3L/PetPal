package com.example.petpal;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class Community extends AppCompatActivity {

    private static final String TAG = "MQTT";
    private MqttClient mqttClient;
    private EditText editTextMessage;
    private LinearLayout messageContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        editTextMessage = findViewById(R.id.editTextMessage);
        messageContainer = findViewById(R.id.messageContainer);
        Button buttonSend = findViewById(R.id.buttonSend);

        String serverUri = "tcp://test.mosquitto.org:1883"; // Cambiar al broker adecuado
        String clientId = MqttClient.generateClientId();

        try {
            mqttClient = new MqttClient(serverUri, clientId, null);

            // Establecer el callback para manejar la recepción de mensajes
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "Conexión perdida", cause);
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String messagePayload = new String(message.getPayload());

                    // Filtrar el mensaje JSON específico que quieres ocultar
                    if (messagePayload.contains("Párkány") && messagePayload.contains("weblink")) {
                        // Si el mensaje contiene las palabras clave de ese JSON específico, no lo mostramos
                        return;
                    }

                    // Comprobar si el mensaje es uno que has enviado tú (identificado por un prefijo único)
                    if (messagePayload.startsWith("sent:")) {
                        return;  // Ignorar mensajes enviados por ti
                    }

                    Log.d(TAG, "Mensaje recibido: " + messagePayload);
                    runOnUiThread(() -> {
                        // Crear un nuevo LinearLayout para alinear el mensaje
                        LinearLayout messageLayout = new LinearLayout(Community.this);
                        messageLayout.setOrientation(LinearLayout.HORIZONTAL);

                        // Crear un TextView para mostrar el mensaje recibido
                        TextView receivedMessage = new TextView(Community.this);
                        receivedMessage.setText(messagePayload);
                        receivedMessage.setBackgroundColor(getResources().getColor(android.R.color.white)); // Color verde para el mensaje recibido
                        receivedMessage.setTextColor(getResources().getColor(android.R.color.black));
                        receivedMessage.setPadding(16, 8, 16, 8);

                        // Alinear el mensaje recibido a la izquierda
                        messageLayout.setGravity(Gravity.START);  // Esto alinea el mensaje recibido a la izquierda

                        messageLayout.addView(receivedMessage);
                        messageContainer.addView(messageLayout);
                    });
                }


                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Confirmación de entrega
                }
            });

            // Conectar al broker
            connectToBroker();

        } catch (MqttException e) {
            Log.e(TAG, "Error al inicializar el cliente MQTT", e);
        }

        // Configurar el botón para enviar el mensaje
        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString();
            if (!message.isEmpty()) {
                publishMessage(message);
            }
        });
    }

    private void connectToBroker() {
        new Thread(() -> {
            try {
                MqttConnectOptions options = new MqttConnectOptions();
                options.setCleanSession(true);
                mqttClient.connect(options);

                // Suscribirse a un tema después de la conexión
                mqttClient.subscribe("test/topic", 1);
                Log.d(TAG, "Conectado al broker");

            } catch (MqttException e) {
                Log.e(TAG, "Error al conectar al broker", e);
            }
        }).start();
    }

    private void publishMessage(String message) {
        new Thread(() -> {
            try {
                // Prefijar el mensaje para indicar que fue enviado por el usuario
                String messageToSend = "sent:" + message;

                // Publicar el mensaje en el broker MQTT
                MqttMessage mqttMessage = new MqttMessage(messageToSend.getBytes());
                mqttMessage.setQos(1);
                mqttClient.publish("test/topic", mqttMessage); // Publica el mensaje
                Log.d(TAG, "Message sent: " + messageToSend);

                // Crear el layout para mostrar el mensaje enviado en la derecha
                runOnUiThread(() -> {
                    LinearLayout messageLayout = new LinearLayout(Community.this);
                    messageLayout.setOrientation(LinearLayout.HORIZONTAL);

                    TextView sentMessage = new TextView(Community.this);
                    sentMessage.setText(message);
                    sentMessage.setBackgroundColor(getResources().getColor(android.R.color.white)); // Color blanco para el mensaje enviado
                    sentMessage.setTextColor(getResources().getColor(android.R.color.black));
                    sentMessage.setPadding(16, 8, 16, 8);

                    // Alinear los mensajes enviados a la derecha
                    messageLayout.setGravity(Gravity.END);  // Esto alinea el mensaje enviado a la derecha

                    messageLayout.addView(sentMessage);
                    messageContainer.addView(messageLayout);

                    // Limpiar el campo de texto
                    editTextMessage.setText("");
                });
            } catch (MqttException e) {
                Log.e(TAG, "Error publishing message", e);
            }
        }).start();
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (MqttException e) {
            Log.e(TAG, "Error al desconectar", e);
        }
    }
}
