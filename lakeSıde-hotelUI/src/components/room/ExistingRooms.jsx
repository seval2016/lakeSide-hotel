// eslint-disable react/prop-types 
// eslint-disable-next-line no-unused-vars
import React, { useEffect, useState } from "react";
import { deleteRoom, getAllRooms } from "../utils/ApiFunctions";
import { Col, Row } from "react-bootstrap";
import RoomFilter from "../common/RoomFilter";
import RoomPaginator from "../common/RoomPaginator";
import { FaEdit, FaEye, FaPlus, FaTrashAlt } from "react-icons/fa";
import { Link } from "react-router-dom";


const ExistingRooms = () => {
	// React bileşeni tanımlanıyor.

	// Oda bilgileri, filtrelenen odalar ve UI durumları için state'ler tanımlanıyor.
	const [rooms, setRooms] = useState([{ id: "", roomType: "", roomPrice: "" }]); // Tüm odalar
	const [currentPage, setCurrentPage] = useState(1); // Mevcut sayfa
	const [roomsPerPage] = useState(8); // Her sayfadaki oda sayısı
	const [isLoading, setIsLoading] = useState(false); // Yükleme durumu
	const [filteredRooms, setFilteredRooms] = useState([{ id: "", roomType: "", roomPrice: "" }]); // Filtrelenmiş odalar
	// eslint-disable-next-line no-unused-vars
	const [selectedRoomType, setSelectedRoomType] = useState(""); // Seçili oda türü
	const [errorMessage, setErrorMessage] = useState(""); // Hata mesajı
	const [successMessage, setSuccessMessage] = useState(""); // Başarı mesajı

	// Bileşen yüklendiğinde odaları getiren `fetchRooms` işlevi çağrılıyor.
	useEffect(() => {
		fetchRooms();
	}, []);

	const fetchRooms = async () => {
		// Odaları API'den getirme işlevi.
		setIsLoading(true); // Yükleme durumu başlatılıyor.
		try {
			const result = await getAllRooms(); // API'den odalar getiriliyor.
			setRooms(result); // Gelen veriler state'e atanıyor.
			setIsLoading(false); // Yükleme durumu sona erdiriliyor.
		} catch (error) {
			setErrorMessage(error.message); // Hata mesajı ayarlanıyor.
			setIsLoading(false);
		}
	};

	useEffect(() => {
		// Seçili oda türü veya odalar değiştiğinde filtreleme yapılır.
		if (selectedRoomType === "") {
			// Oda türü seçilmediyse tüm odalar gösterilir.
			setFilteredRooms(rooms);
		} else {
			// Seçili oda türüne göre filtreleme yapılır.
			const filteredRooms = rooms.filter((room) => room.roomType === selectedRoomType);
			setFilteredRooms(filteredRooms);
		}
		setCurrentPage(1); // Sayfa numarası sıfırlanır.
	}, [rooms, selectedRoomType]);

	const handlePaginationClick = (pageNumber) => {
		// Sayfa numarası değiştirildiğinde state güncellenir.
		setCurrentPage(pageNumber);
	};

	const handleDelete = async (roomId) => {
		// Oda silme işlevi.
		try {
			const result = await deleteRoom(roomId); // API çağrısı ile oda silinir.
			if (result === "") {
				// Başarılı bir şekilde silindiğinde başarı mesajı gösterilir.
				setSuccessMessage(`Room No ${roomId} was deleted`);
				fetchRooms(); // Odalar tekrar yüklenir.
			} else {
				// Hata oluştuğunda konsola yazdırılır.
				console.error(`Error deleting room : ${result.message}`);
			}
		} catch (error) {
			setErrorMessage(error.message); // Hata mesajı gösterilir.
		}
		// Mesajlar 3 saniye sonra sıfırlanır.
		setTimeout(() => {
			setSuccessMessage("");
			setErrorMessage("");
		}, 3000);
	};

	const calculateTotalPages = (filteredRooms, roomsPerPage, rooms) => {
		// Toplam sayfa sayısını hesaplar.
		const totalRooms = filteredRooms.length > 0 ? filteredRooms.length : rooms.length;
		return Math.ceil(totalRooms / roomsPerPage);
	};

	// Sayfalandırma için odaların başlangıç ve bitiş indeksleri hesaplanır.
	const indexOfLastRoom = currentPage * roomsPerPage; // Mevcut sayfadaki son oda
	const indexOfFirstRoom = indexOfLastRoom - roomsPerPage; // Mevcut sayfadaki ilk oda
	const currentRooms = filteredRooms.slice(indexOfFirstRoom, indexOfLastRoom); // Mevcut sayfadaki odalar

	return (
		<>
			{/* Başarı ve hata mesajları gösteriliyor. */}
			<div className="container col-md-8 col-lg-6">
				{successMessage && <p className="alert alert-success mt-5">{successMessage}</p>}
				{errorMessage && <p className="alert alert-danger mt-5">{errorMessage}</p>}
			</div>

			{/* Yükleme durumu gösteriliyor. */}
			{isLoading ? (
				<p>Loading existing rooms</p>
			) : (
				<>
					{/* Odalar listesi ve filtreleme/sayfalama bileşenleri */}
					<section className="mt-5 mb-5 container">
						<div className="d-flex justify-content-between mb-3 mt-5">
							<h2>Existing Rooms</h2>
						</div>

						{/* Filtreleme ve oda ekleme bağlantısı */}
						<Row>
							<Col md={6} className="mb-2 md-mb-0">
								<RoomFilter data={rooms} setFilteredData={setFilteredRooms} />
							</Col>
							<Col md={6} className="d-flex justify-content-end">
								<Link to={"/add-room"}>
									<FaPlus /> Add Room
								</Link>
							</Col>
						</Row>

						{/* Oda tablosu */}
						<table className="table table-bordered table-hover">
							<thead>
								<tr className="text-center">
									<th>ID</th>
									<th>Room Type</th>
									<th>Room Price</th>
									<th>Actions</th>
								</tr>
							</thead>
							<tbody>
								{/* Mevcut odalar tablo satırlarında gösteriliyor. */}
								{currentRooms.map((room) => (
									<tr key={room.id} className="text-center">
										<td>{room.id}</td>
										<td>{room.roomType}</td>
										<td>{room.roomPrice}</td>
										<td className="gap-2">
											{/* Düzenle ve Sil butonları */}
											<Link to={`/edit-room/${room.id}`} className="gap-2">
												<span className="btn btn-info btn-sm">
													<FaEye />
												</span>
												<span className="btn btn-warning btn-sm ml-5">
													<FaEdit />
												</span>
											</Link>
											<button
												className="btn btn-danger btn-sm ml-5"
												onClick={() => handleDelete(room.id)}>
												<FaTrashAlt />
											</button>
										</td>
									</tr>
								))}
							</tbody>
						</table>

						{/* Sayfalama bileşeni */}
						<RoomPaginator
							currentPage={currentPage}
							totalPages={calculateTotalPages(filteredRooms, roomsPerPage, rooms)}
							onPageChange={handlePaginationClick}
						/>
					</section>
				</>
			)}
		</>
	);
};

export default ExistingRooms;
// `ExistingRooms` bileşeni dışa aktarılır.
