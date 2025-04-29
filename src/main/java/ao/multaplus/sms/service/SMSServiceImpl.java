package ao.multaplus.sms.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

@Service
public class SMSServiceImpl implements SMSService {

    private final String senderId;
    private final String token;
    private final String MIMO_URL;

    public SMSServiceImpl(@Value("${api.mimo.token}") String token,
                          @Value("${api.security.mimo.sender.id}") String senderId,
                          @Value("${api.mimi.Baseurl}") String MIMO_URL) {
        this.MIMO_URL = MIMO_URL;
        this.senderId = senderId;
        this.token = token;

    }

    @Override
    public void sendSMSWithMimoRestAip(String to, String message) {
        String smsUrl = String.format(
                "%s/message/send?token=%s&sender=%s&recipients=%s&text=%s", MIMO_URL,
                token, senderId, to, message);
        try {
            RestClient.create()
                    .get()
                    .uri(smsUrl)
                    .retrieve()
                    .toBodilessEntity();
        } catch (RestClientException e) {
            System.err.println("Error send message: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error send message", e);
        }
    }


}

