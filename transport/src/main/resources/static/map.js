(() => {
	const mapElement = document.getElementById("shipment-map");
	if (!mapElement || typeof L === "undefined") return;

	const map = L.map(mapElement, { scrollWheelZoom: false }).setView([23.8, -102.5], 5);
	L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
		attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a>'
	}).addTo(map);

	const colors = {
		IN_TRANSIT: "#2868ee",
		DELAYED: "#ed9940",
		DELIVERED: "#21ad82",
		PREPARING: "#8a74d5"
	};
	const points = [];

	document.querySelectorAll(".shipment-marker").forEach((marker) => {
		const latitude = Number(marker.dataset.lat);
		const longitude = Number(marker.dataset.lng);
		if (!Number.isFinite(latitude) || !Number.isFinite(longitude)) return;
		const color = colors[marker.dataset.status] || colors.PREPARING;
		L.circleMarker([latitude, longitude], {
			radius: 8,
			color: "#ffffff",
			weight: 3,
			fillColor: color,
			fillOpacity: 1
		}).bindPopup(
			`<strong>${escapeHtml(marker.dataset.number)}</strong><br>` +
			`${escapeHtml(marker.dataset.customer)}<br>` +
			`${escapeHtml(marker.dataset.route)}<br>` +
			`<span style="color:${color}">${escapeHtml(marker.dataset.label)}</span>`
		).addTo(map);
		points.push([latitude, longitude]);
	});

	if (points.length > 1) map.fitBounds(points, { padding: [25, 25], maxZoom: 7 });
	else if (points.length === 1) map.setView(points[0], 7);

	function escapeHtml(value) {
		return String(value || "").replace(/[&<>"']/g, (character) => ({
			"&": "&amp;", "<": "&lt;", ">": "&gt;", '"': "&quot;", "'": "&#39;"
		})[character]);
	}
})();
