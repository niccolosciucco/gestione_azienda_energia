package bw_team7.gestione_azienda_energia.email;

import kong.unirest.core.HttpResponse;
import kong.unirest.core.JsonNode;
import kong.unirest.core.Unirest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailgunSender {

    private final String mailgunApiKey;
    private final String mailgunDomainName;

    public MailgunSender(
            @Value("${mailgun.apiKey}") String mailgunApiKey,
            @Value("${mailgun.domain}") String mailgunDomainName) {
        this.mailgunApiKey = mailgunApiKey;
        this.mailgunDomainName = mailgunDomainName;
    }

    public void sendEmail(String recipient, String subject, String bodyText) {
        System.out.println("--- DEBUG MAILGUN ---");
        System.out.println("Domain usato: " + this.mailgunDomainName);
        System.out.println("API Key usata: " + this.mailgunApiKey);

        HttpResponse<JsonNode> response = Unirest.post("https://api.mailgun.net/v3/" + this.mailgunDomainName + "/messages")
                .basicAuth("api", this.mailgunApiKey)
                .queryString("from", "TEAM 7 <mailgun@" + this.mailgunDomainName + ">")
                .queryString("to", recipient)
                .queryString("subject", subject)
                .queryString("text", bodyText)
                .asJson();

        System.out.println("Status Code reale: " + response.getStatus());
        System.out.println("Body della risposta: " + response.getBody());
        System.out.println("---------------------");
    }
}