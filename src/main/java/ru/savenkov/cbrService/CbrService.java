package ru.savenkov.cbrService;

import org.jetbrains.annotations.NotNull;
import retrofit.http.GET;
import retrofit.http.Query;
import ru.savenkov.dto.ValCurs;

//данный интерфейс обозначает, что мы будем запрашивать (GET) данные с ЦБ
//ссылка получения данных - https://www.cbr.ru/scripts/XML_daily.asp?date_req=02/03/2002
// https: Протокол
// www.cbr.ru: доменное имя
// scripts/XML_daily.asp: файл на узле (путь до контроллера)
// date_req=02/03/2002 : GET параметр

//2 основных вида HTTP запроса:
//GET - параметры передаются в строке запроса
//POST - параметры передаются в теле запроса
public interface CbrService {
    @GET("/scripts/XML_daily.asp") //говорим, что данный метод будет запрашивать данные с файла на узле </scripts/XML_daily.asp>
    public @NotNull ValCurs getExchange(@Query("date_req") @NotNull String date);
}
