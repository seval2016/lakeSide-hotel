/* eslint-disable no-unused-vars */
import axios from "axios";

/**
 *  axios.create() metodu ile bir api nesnesi oluşturulur.
 *  Bu nesne, temel URL (baseURL) olarak http://localhost:3000 değerini kullanır. 
 *  Yani, bu nesneyle yapılan tüm HTTP istekleri bu base URL üzerinden yapılacaktır.
 *  Örneğin, /rooms/add/new-room şeklinde bir endpoint çağrısı yapıldığında, 
 bu istek aslında http://localhost:3000/rooms/add/new-room adresine gönderilecektir.
 */
export const api = axios.create({
  baseURL: "http://localhost:3000",
});

/** 
 * Bu, name adında bir asenkron fonksiyon tanımlar. Bu fonksiyon, 
 * 3 parametre alır: photo (oda fotoğrafı), roomType (oda tipi), ve roomPrice (oda fiyatı). 
 */
export async function name(photo,roomType,roomPrice) {
    /**
     * Bu, name adında bir asenkron fonksiyon tanımlar. Bu fonksiyon, 3 parametre alır: 
     * photo (oda fotoğrafı), roomType (oda tipi), ve roomPrice (oda fiyatı).
     */
    const formData=new FormData()
    formData.append("photo" , photo)
    formData.append("roomType" , roomType)
    formData.append("roomPrice" , roomPrice)

    /**
     * Bu satırlar, FormData nesnesi oluşturur. 
     * FormData, verileri form verisi formatında göndermek için kullanılır. 
     * append() metodu ile fotoğraf, oda tipi ve oda fiyatı gibi veriler formData nesnesine eklenir. 
     * Bu, özellikle dosya (fotoğraf) gönderirken kullanılır.
     */

    const response= await api.post("/rooms/add/new-room",formData)
    if(response.status === 201){
        return true
    }else{
        return false
    }
    /**
     * Açıklama: response.status ile HTTP yanıt kodu kontrol edilir. 
     * Eğer yanıt kodu 201 (yeni bir kaynak başarıyla oluşturulmuş) ise, fonksiyon true döner.
     * Aksi takdirde false döner. Bu, API isteğinin başarılı olup olmadığını kontrol etmek için yapılır.
     */
    
}

export async function getRoomTypes() {
    try {
        const response =await api.get("/rooms/room-types")
        return response.data
    } catch (error) {
        throw new Error ("Error fetching room types")
        
    }
    /**
     * api.get() metodu ile http://localhost:3000/rooms/room-types adresine GET isteği gönderilir. 
     * Bu istek, oda tiplerini almak için yapılır ve sonuç response değişkeninde saklanır.
     */
}