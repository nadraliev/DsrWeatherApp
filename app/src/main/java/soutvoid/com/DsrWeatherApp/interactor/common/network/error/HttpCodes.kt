package soutvoid.com.DsrWeatherApp.interactor.common.network.error

object HttpCodes {
    val CODE_200 = 200 //успех
    val CODE_304 = 304 //нет обновленных данных
    val CODE_401 = 401 //недоступное действие для пользователя
    val CODE_400 = 400 //Bad request, возможно передан невалидный токен
    val CODE_500 = 500 //ошибка сервера
}
