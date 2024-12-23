/* eslint-disable no-unused-vars */
import axios from "axios"; // Axios kütüphanesi, HTTP istekleri yapmak için kullanılıyor.


export const api = axios.create({
    baseURL: "http://localhost:9192"
});

// Token'ı localStorage'dan alarak bir header döndüren fonksiyon.
export const getHeader = () => {
    const token = localStorage.getItem("token"); // Kullanıcının token'ı localStorage'dan alınır.
    return {
        Authorization: `Bearer ${token}`, // Bearer token, yetkilendirme için kullanılır.
        "Content-Type": "application/json" // JSON formatında veri gönderileceği belirtilir.
    };
};

// Yeni bir oda ekleme fonksiyonu
export async function addRoom(photo, roomType, roomPrice) {
    const formData = new FormData(); // FormData nesnesi oluşturuluyor, çünkü dosya yüklenecek.
    formData.append("photo", photo); // Fotoğraf formData'ya ekleniyor.
    formData.append("roomType", roomType); // Oda türü ekleniyor.
    formData.append("roomPrice", roomPrice); // Oda fiyatı ekleniyor.

    // POST isteği ile yeni oda ekleniyor.
    const response = await api.post("/rooms/add/new-room", formData, {
        headers: getHeader() // Header ile birlikte gönderim yapılıyor.
    });

    // Yanıt durumuna göre işlem başarılı mı kontrol ediliyor.
    if (response.status === 201) {
        return true; // Oda başarıyla eklendi.
    } else {
        return false; // Oda eklenemedi.
    }
}

// Tüm oda türlerini veritabanından getiren fonksiyon
export async function getRoomTypes() {
    try {
        const response = await api.get("/rooms/room/types"); // Oda türleri için GET isteği yapılıyor.
        return response.data; // Gelen veri döndürülüyor.
    } catch (error) {
        throw new Error("Error fetching room types"); // Hata durumunda bir mesaj oluşturuluyor.
    }
}

// Tüm odaları veritabanından getiren fonksiyon
export async function getAllRooms() {
    try {
        const result = await api.get("/rooms/all-rooms"); // Tüm odalar için GET isteği yapılıyor.
        return result.data; // Gelen veriler döndürülüyor.
    } catch (error) {
        throw new Error("Error fetching rooms"); // Hata durumunda bir mesaj oluşturuluyor.
    }
}

// Belirtilen oda ID'sine göre oda silen fonksiyon
export async function deleteRoom(roomId) {
    try {
        const result = await api.delete(`/rooms/delete/room/${roomId}`, {
            headers: getHeader() // Yetkilendirme header'ı ile birlikte istek gönderiliyor.
        });
        return result.data; // Başarılı sonuç döndürülüyor.
    } catch (error) {
        throw new Error(`Error deleting room ${error.message}`); // Hata durumunda mesaj oluşturuluyor.
    }
}

// Oda güncelleme fonksiyonu
export async function updateRoom(roomId, roomData) {
    const formData = new FormData(); // FormData kullanılıyor çünkü fotoğraf yükleniyor.
    formData.append("roomType", roomData.roomType); // Yeni oda türü ekleniyor.
    formData.append("roomPrice", roomData.roomPrice); // Yeni oda fiyatı ekleniyor.
    formData.append("photo", roomData.photo); // Yeni fotoğraf ekleniyor.

    const response = await api.put(`/rooms/update/${roomId}`, formData, {
        headers: getHeader() // Header ekleniyor.
    });

    return response; // Yanıt döndürülüyor.
}

// Belirtilen ID'ye göre bir oda getiren fonksiyon
export async function getRoomById(roomId) {
    try {
        const result = await api.get(`/rooms/room/${roomId}`); // Oda bilgisi için GET isteği yapılıyor.
        return result.data; // Oda bilgisi döndürülüyor.
    } catch (error) {
        throw new Error(`Error fetching room ${error.message}`); // Hata durumunda mesaj oluşturuluyor.
    }
}

// Yeni bir rezervasyon kaydı ekleyen fonksiyon
export async function bookRoom(roomId, booking) {
    try {
        const response = await api.post(`/bookings/room/${roomId}/booking`, booking); // POST isteği ile rezervasyon yapılıyor.
        return response.data; // Başarılı sonuç döndürülüyor.
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data); // Backend'den gelen hata mesajı döndürülüyor.
        } else {
            throw new Error(`Error booking room : ${error.message}`); // Diğer hatalar için genel mesaj.
        }
    }
}

// Tüm rezervasyonları getiren fonksiyon
export async function getAllBookings() {
    try {
        const result = await api.get("/bookings/all-bookings", {
            headers: getHeader() // Header ekleniyor.
        });
        return result.data; // Rezervasyonlar döndürülüyor.
    } catch (error) {
        throw new Error(`Error fetching bookings : ${error.message}`); // Hata durumunda mesaj oluşturuluyor.
    }
}

// Rezervasyonun onay koduna göre rezervasyon bilgisi getiren fonksiyon
export async function getBookingByConfirmationCode(confirmationCode) {
    try {
        const result = await api.get(`/bookings/confirmation/${confirmationCode}`); // Onay koduna göre GET isteği.
        return result.data; // Rezervasyon bilgisi döndürülüyor.
    } catch (error) {
        if (error.response && error.response.data) {
            throw new Error(error.response.data); // Backend'den hata mesajı.
        } else {
            throw new Error(`Error find booking : ${error.message}`); // Genel hata mesajı.
        }
    }
}

// Kullanıcı rezervasyonunu iptal eden fonksiyon
export async function cancelBooking(bookingId) {
    try {
        const result = await api.delete(`/bookings/booking/${bookingId}/delete`); // DELETE isteği ile rezervasyon iptal ediliyor.
        return result.data; // Başarılı sonuç döndürülüyor.
    } catch (error) {
        throw new Error(`Error cancelling booking :${error.message}`); // Hata mesajı oluşturuluyor.
    }
}

// Belirtilen tarihler ve oda türüne göre uygun odaları getiren fonksiyon
export async function getAvailableRooms(checkInDate, checkOutDate, roomType) {
    const result = await api.get(
        `rooms/available-rooms?checkInDate=${checkInDate}
        &checkOutDate=${checkOutDate}&roomType=${roomType}`
    ); // GET isteği ile uygun odalar aranıyor.
    return result; // Sonuç döndürülüyor.
}

// Kullanıcı kaydı yapan fonksiyon
export async function registerUser(registration) {
    try {
        const response = await api.post("/auth/register-user", registration); // POST isteği ile kullanıcı kaydediliyor.
        return response.data; // Kullanıcı bilgisi döndürülüyor.
    } catch (error) {
        if (error.reeponse && error.response.data) {
            throw new Error(error.response.data); // Backend'den gelen hata mesajı.
        } else {
            throw new Error(`User registration error : ${error.message}`); // Genel hata mesajı.
        }
    }
}

// Kullanıcı giriş işlemini yapan fonksiyon
export async function loginUser(login) {
    try {
        const response = await api.post("/auth/login", login); // Giriş için POST isteği.
        if (response.status >= 200 && response.status < 300) {
            return response.data; // Başarılı giriş bilgisi döndürülüyor.
        } else {
            return null; // Hata durumunda null döndürülüyor.
        }
    } catch (error) {
        console.error(error); // Hata konsola yazdırılıyor.
        return null; // Hata durumunda null döndürülüyor.
    }
}

// Kullanıcı ID'sine göre rezervasyonları getiren fonksiyon
export async function getBookingsByUserId(userId, token) {
    try {
        const response = await api.get(`/bookings/user/${userId}/bookings`, {
            headers: getHeader() // Header ekleniyor.
        });
        return response.data; // Kullanıcı rezervasyonları döndürülüyor.
    } catch (error) {
        console.error("Error fetching bookings:", error.message); // Hata konsola yazdırılıyor.
        throw new Error("Failed to fetch bookings"); // Hata mesajı oluşturuluyor.
    }
}
