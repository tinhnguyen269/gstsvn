// contact.js
document.addEventListener("DOMContentLoaded", function () {
    // Load customer info to form
    document.querySelectorAll('.edit').forEach(btn => {
        btn.addEventListener('click', function () {
            const customerId = this.dataset.id;
            fetch(`/admin/contact/edit/${customerId}`)
                .then(res => res.json())
                .then(data => {
                    document.getElementById('customerId').value = data.customerId;
                    document.getElementById('name').value = data.name;
                    document.getElementById('phoneNumber').value = data.phoneNumber;
                    document.getElementById('serviceId').value = data.serviceId;
                    document.getElementById('context').value = data.context;
                    document.getElementById('status').value = data.status;
                })
                .catch(() => alert("Không thể lấy dữ liệu khách hàng."));
        });
    });

    // Submit update form
    const editForm = document.getElementById('editForm');
    if (editForm) {
        editForm.addEventListener('submit', function (e) {
            e.preventDefault();

            const customerData = {
                customerId: editForm.customerId.value,
                name: editForm.name.value,
                phoneNumber: editForm.phoneNumber.value,
                serviceId: editForm.serviceId.value,
                context: editForm.context.value,
                status: editForm.status.value
            };

            fetch(`/admin/contact/update/${customerData.customerId}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(customerData)
            })
                .then(res => {
                    if (res.ok) {
                        $('#editContactModal').modal('hide');
                        location.reload();
                    } else {
                        throw new Error();
                    }
                })
                .catch(() => alert("Cập nhật thất bại."));
        });
    }
});
