package com.example.shoppingtesting.data.local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ShoppingDao {

    // Database'e veriler ekleniyor - Adding datas to database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(shoppingItem: ShoppingItem)

    // Database'den verileri silme - Delete datas from database
    @Delete
    suspend fun deleteShoppingItem(shoppingItem: ShoppingItem)

    // Database'den gözlemlenebilir (observable) olarak tüm verileri getir. - Bring all datas from database as observable
    @Query("select * from shopping_items")
    fun observeAllShoppingItem(): LiveData<List<ShoppingItem>>

    // Database'deki fiyat ve ürün miktarını çarp toplam maliyeti getir - Bring total price, multiplication the price and amount of the product
    @Query("select sum(price * amount) from shopping_items")
    fun observeTotalPrice(): LiveData<Float>


}