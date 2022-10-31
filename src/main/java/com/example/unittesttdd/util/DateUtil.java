package com.example.unittesttdd.util;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtil {

    public boolean excedeuPrazo(LocalDateTime dataPrevista, LocalDateTime dataDevolucao){
        return dataDevolucao.isAfter(dataPrevista) ? true : false;
    }

    public int diasEntreDatas(LocalDateTime inicio, LocalDateTime fim){
        return Math.toIntExact( ChronoUnit.DAYS.between(fim, inicio) );
    }
}
