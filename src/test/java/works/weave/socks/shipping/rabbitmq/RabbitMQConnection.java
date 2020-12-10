package works.weave.socks.shipping.rabbitmq;

import com.rabbitmq.client.Channel;
import org.junit.Test;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

public class RabbitMQConnection {
    @Test
    public void testConnection() throws URISyntaxException {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(new URI("amqps://" + System.getenv("RABBIT_MQ_HOSTNAME") + ":" + System.getenv("RABBIT_MQ_PORT")));
        connectionFactory.setCloseTimeout(5000);
        connectionFactory.setConnectionTimeout(5000);
        connectionFactory.setUsername(System.getenv("RABBIT_MQ_USERNAME"));
        connectionFactory.setPassword(System.getenv("RABBIT_MQ_PASSWORD"));
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.execute(new ChannelCallback<String>() {
            @Override
            public String doInRabbit(Channel channel) throws Exception {
                Map<String, Object> serverProperties = channel.getConnection().getServerProperties();
                return serverProperties.get("version").toString();
            }
        });
    }
}
