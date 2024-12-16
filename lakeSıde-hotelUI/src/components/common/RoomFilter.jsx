/* eslint-disable react/prop-types */
// eslint-disable-next-line no-unused-vars
import React, { useState } from "react"

// RoomFilter bileşeni, data ve setFilteredData propslarını alır. 
// data: Tüm odaların listesini içerir.
// setFilteredData: Filtrelenen odaların listesini ayarlamak için kullanılır.
const RoomFilter = ({ data, setFilteredData }) => {
  // filter: Kullanıcının seçtiği filtreyi tutan state.
  const [filter, setFilter] = useState("")

  // Kullanıcı bir filtre seçtiğinde tetiklenir.
  const handleSelectChange = (e) => {
    const selectedType = e.target.value // Seçilen oda türü alınır.
    setFilter(selectedType) // filter state'i güncellenir.

    // Seçilen türe göre odalar filtrelenir.
    const filteredRooms = data.filter((room) =>
      room.roomType.toLowerCase().includes(selectedType.toLowerCase())
    )
    setFilteredData(filteredRooms) // Filtrelenmiş oda verisi gönderilir.
  }

  // Filtreyi temizlemek için kullanılır.
  const clearFilter = () => {
    setFilter("") // Filtreleme state'i sıfırlanır.
    setFilteredData(data) // Tüm odalar gösterilir.
  }

  // Mevcut oda türlerini çıkarır ve filtreleme seçenekleri için bir liste oluşturur.
  const roomTypes = ["", ...new Set(data.map((room) => room.roomType))]

  // Bileşenin kullanıcıya gösterilen kısmı (UI).
  return (
    <div className="input-group mb-3">
      {/* Kullanıcıya filtreleme özelliğini açıklayan etiket */}
      <span className="input-group-text" id="room-type-filter">
        Filter rooms by type
      </span>

      {/* Oda türlerini seçmek için bir dropdown */}
      <select
        className="form-select"
        aria-label="room type filter"
        value={filter} // Seçili filtre
        onChange={handleSelectChange} // Değişiklik olduğunda tetiklenen fonksiyon
      >
        <option value="">Select a room type to filter...</option>
        {roomTypes.map((type, index) => (
          <option key={index} value={String(type)}>
            {String(type)} {/* Her oda türünü seçenek olarak listeye ekler */}
          </option>
        ))}
      </select>

      {/* Filtreyi temizlemek için bir düğme */}
      <button className="btn btn-hotel" type="button" onClick={clearFilter}>
        Clear Filter
      </button>
    </div>
  )
}

export default RoomFilter
