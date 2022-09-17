package com.example.shoppingtesting.other

// Burdaki Resource Generic Class'ı ile aslında Error Handling (Hata İşleme) yani State Handling (Durum İşleme) olmuş oluyor
// Buradaki Resource Generic Class'ında Kaynak kodumuzda api üzerindeki verilerin Success, Loading, Error durumlarının kullanımı için böyle bir sınıf oluşturuldu.
// Burdaki Resource Generic Class'ının içerisindeki fonksiyonlar sayesinde Repositoryde, ViewModelde ve UI'da kullanıp apiden gelen verileri ilgili durumlara göre çalıştırmaya çalışıyoruz.
// Bu sınıf tüm farklı projelerimizde kullanıbilir.
class Resource<out T>(val status: Status, val data: T?, val message: String?) {

    companion object {

        fun <T> success(data: T?): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }
    }
}

enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}