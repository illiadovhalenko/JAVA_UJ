package uj.wmii.pwj.delegations;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Calc {
    BigDecimal calculate(String name, String start, String end, BigDecimal dailyRate) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm VV");
        Duration duration = Duration.between(ZonedDateTime.parse(start, dateTimeFormatter), ZonedDateTime.parse(end, dateTimeFormatter));
        long numberOfDays = duration.toDays();
        long numberOfHours = duration.minusDays(numberOfDays).toHours();
        long numberOfMinutes = duration.minusDays(numberOfDays).minusHours(numberOfHours).toMinutes();
        BigDecimal salary = BigDecimal.valueOf(numberOfDays).multiply(dailyRate);
        if(numberOfHours > 12) {
            salary = salary.add(dailyRate);
        }else if(numberOfHours > 8){
            salary = salary.add(dailyRate.divide(BigDecimal.valueOf(2), RoundingMode.HALF_UP));
        }else if(numberOfHours > 0 || numberOfMinutes > 0){
            salary = salary.add(dailyRate.divide(BigDecimal.valueOf(3), RoundingMode.HALF_UP));
        }
        return salary;
    }
}
