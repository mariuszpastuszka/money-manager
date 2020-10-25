package pl.sda.moneymanager.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile({"nbp", "exchange"})
public class MoneyExchangerService implements CommandLineRunner {
    @Override
    public void run(String... args) throws Exception {
        log.info("money exchanger service created");
    }
}
