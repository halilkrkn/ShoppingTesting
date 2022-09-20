package com.example.shoppingtesting.other

/*  EVENT Sınıfımızın amacı;
  - Temel olarak livedata'nın tek seferlik olaylar yaymasını sağlamaktadır.
  - Bu nedenle genellikle sunucumuza bir ağ isteğinde bulunurken sorun yaşarız ve ardından bu Resource Class'ıyla sonunda Success veya Error 'u yayarız.
  - Bu nedenle livedata object'i ya bir Succes kaynağı ya da bir Error kaynağı tutar.
  - Oluşturduğumuz modellerde livedata kullandığımız için livedata kullanımı daha net bir şekilde çalışmasını sağlatmaya çalışıyoruz.
 */


open class Event <out T>(private val content: T) {

    var hasBeenHandled = false
        private set // Allow external read but not write - Dışarıdan okuma olayına izin verir ama yazma olayına izin vermez.

    /*
    * Returns the content and prevents its use again. - İçeriği döndürür ve tekrar kullanılmasını engeller.
     */
    fun getContentIfNotHandled(): T? {
        return if (hasBeenHandled) {
            null
        } else {
            hasBeenHandled = true
            content
        }
    }

    /*
    * Returns the content, even if it's already been handled. - Daha önce işlenmiş olsa bile içeriği döndürür.
     */
    fun peekContent(): T = content
}
