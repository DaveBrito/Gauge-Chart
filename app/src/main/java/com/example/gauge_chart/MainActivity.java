package com.example.gauge_chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.ekn.gruzer.gaugelibrary.HalfGauge;
import com.ekn.gruzer.gaugelibrary.Range;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MQTT"; // Tag para os logs

    Button idBtnSubir, idBtnBaixar;
    HalfGauge idMedidor;
    Range Rango_1, Rango_2, Rango_3;
    int SetearGrafica = 0;

    private MqttClient arduinoClient;
    private static final String ARDUINO_MQTT_TOPIC = "mqttHQ-client-testt"; // Tópico MQTT para o dispositivo Arduino

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        connectToMqttBroker();

        idBtnSubir = findViewById(R.id.idBtnSubir);
        idBtnBaixar = findViewById(R.id.idBtnBaixar);
        idMedidor = findViewById(R.id.idMedidor);

        Rango_1 = new Range();
        Rango_2 = new Range();
        Rango_3 = new Range();

        Rango_1.setFrom(0);
        Rango_1.setTo(100);
        Rango_2.setFrom(100);
        Rango_2.setTo(150);
        Rango_3.setFrom(150);
        Rango_3.setTo(200);

        Rango_1.setColor(Color.GREEN);
        Rango_2.setColor(Color.BLUE);
        Rango_3.setColor(Color.RED);
        idMedidor.setMinValue(0);
        idMedidor.setMaxValue(200);
        idMedidor.setValue(0);

        idMedidor.addRange(Rango_1);
        idMedidor.addRange(Rango_2);
        idMedidor.addRange(Rango_3);

        idBtnSubir.setOnClickListener(v -> {
            SetearGrafica += 10;
            if (SetearGrafica > 200) {
                SetearGrafica = 200;
            }
            idMedidor.setValue(SetearGrafica);

            // Enviar a mensagem para o tópico MQTT
            enviarMensagemMQTT(String.valueOf(SetearGrafica));
        });

        idBtnBaixar.setOnClickListener(v -> {
            SetearGrafica -= 10;
            if (SetearGrafica < 0) {
                SetearGrafica = 0;
            }
            idMedidor.setValue(SetearGrafica);

            // Enviar a mensagem para o tópico MQTT
            enviarMensagemMQTT(String.valueOf(SetearGrafica));
        });
    }

    private void connectToMqttBroker() {
        try {
            Log.d(TAG, "Tentando se conectar ao broker MQTT...");
            String brokerUrl = "tcp://public.mqtthq.com:1883";
            String clientId = MqttClient.generateClientId();
            arduinoClient = new MqttClient(brokerUrl, clientId);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            arduinoClient.connect(options);
            Log.d(TAG, "Conexão bem-sucedida ao broker MQTT.");
            arduinoClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.e(TAG, "Conexão MQTT (Arduino) perdida.");
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    String receivedMessage = new String(message.getPayload(), StandardCharsets.UTF_8);
                    Log.d(TAG, "Mensagem recebida do dispositivo Arduino: " + receivedMessage);
                    // Aqui você pode adicionar o código para processar a mensagem recebida, se necessário.
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    // Não é necessário neste caso
                }
            });
            // Inscreva-se no tópico correto para receber mensagens do Arduino
            arduinoClient.subscribe(ARDUINO_MQTT_TOPIC);
        } catch (MqttException e) {
            e.printStackTrace();
            Log.e(TAG, "Erro ao conectar: " + e.getMessage());
        }
    }

    private void enviarMensagemMQTT(String mensagem) {
        try {
            if (arduinoClient != null && arduinoClient.isConnected()) {
                MqttMessage mqttMessage = new MqttMessage();
                mqttMessage.setPayload(mensagem.getBytes());
                arduinoClient.publish(ARDUINO_MQTT_TOPIC, mqttMessage);
                // Adicionando um log para verificar se a mensagem foi enviada corretamente
                Log.d(TAG, "Mensagem MQTT enviada: " + mensagem);
            } else {
                Log.e(TAG, "Erro ao enviar mensagem: Cliente MQTT não está conectado");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (arduinoClient != null && arduinoClient.isConnected()) {
                arduinoClient.disconnect();
                Log.d(TAG, "Desconectado do broker MQTT.");
            }
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
