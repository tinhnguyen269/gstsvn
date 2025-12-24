// Cuọno lên đầu website
    const backToTopBtn = document.getElementById("backToTopBtn");

    window.addEventListener("scroll", () => {
        if (window.scrollY > 200) {
            backToTopBtn.style.display = "block";
        } else {
            backToTopBtn.style.display = "none";
        }
    });

    backToTopBtn.addEventListener("click", () => {
        window.scrollTo({
            top: 0,
            behavior: "smooth"
        });
    });

// Hiệu ứng rung icon contact
document.addEventListener("DOMContentLoaded", () => {
    const icons = document.querySelectorAll(".echbay-sms-messenger div");

    // Thêm hiệu ứng rung xen kẽ cho các icon
    icons.forEach((icon, index) => {
        setTimeout(() => {
            icon.classList.add("ring-animation");
        }, index * 400); // trễ 0.4s giữa mỗi icon
    });
});
// video post và service
document.addEventListener('DOMContentLoaded', (event) => {
    const oembedTags = document.querySelectorAll('oembed');

    oembedTags.forEach(oembed => {
        const videoUrl = oembed.getAttribute('url');

        if (videoUrl) {
            // Lấy ID video (tương tự như trước)
            const urlParts = videoUrl.split('/');
            const videoId = urlParts[urlParts.length - 1].split('-')[0].split('?')[0];

            if (videoId) {
                const embedUrl = `https://www.youtube.com/embed/${videoId}`;

                // 1. TẠO THẺ BAO BỌC DIV (WRAPPER)
                const wrapperDiv = document.createElement('div');
                wrapperDiv.className = 'video-responsive-container'; // Gán lớp CSS đã tạo

                // 2. TẠO PHẦN TỬ IFRAME (Bỏ thuộc tính width/height cố định)
                const iframe = document.createElement('iframe');
                iframe.setAttribute('src', embedUrl);
                iframe.setAttribute('frameborder', '0');
                iframe.setAttribute('allow', 'accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture');
                iframe.setAttribute('allowfullscreen', '');

                // 3. Chèn iframe vào wrapper
                wrapperDiv.appendChild(iframe);

                // 4. Thay thế thẻ <oembed> bằng wrapper div
                oembed.parentNode.replaceChild(wrapperDiv, oembed);
            }
        }
    });
});


// Xử lý gửi form liên hệ với Fetch API và CSRF
    document.getElementById("contactForm").addEventListener("submit", function (e) {

        e.preventDefault();

        // Xóa lỗi cũ
        document.querySelectorAll(".text-danger").forEach(el => el.innerText = "");

        let formData = new FormData(this);

        fetch("/lien-he/them-moi", {
            method: "POST",
            body: formData,

        })
            .then(res => res.json())
            .then(data => {
                if (data.status === "error") {
                    for (let field in data.errors) {
                        let errDiv = document.getElementById("error-" + field);
                        if (errDiv) {
                            errDiv.innerText = data.errors[field];
                        }
                    }
                }
                if (data.status === "success") {
                    let toastEl = document.getElementById("successToast");
                    let toastOverlay = document.getElementById("toastOverlay");
                    let toastMessage = toastEl.querySelector(".toast-message");

                    if (toastMessage) {
                        toastMessage.innerText = data.message;
                    } else {
                        // Fallback nếu không tìm thấy .toast-message
                        let toastBody = toastEl.querySelector(".toast-body");
                        if (toastBody) {
                            toastBody.innerHTML = `<strong class="d-block mb-1">Thành công!</strong><span>${data.message}</span>`;
                        }
                    }

                    // Khởi tạo toast trước
                    let bsToast = new bootstrap.Toast(toastEl, {
                        delay: 3000,
                        autohide: true
                    });

                    // Hiển thị overlay
                    if (toastOverlay) {
                        toastOverlay.style.display = "block";
                        // Đóng toast khi click vào overlay
                        toastOverlay.onclick = function() {
                            bsToast.hide();
                            toastOverlay.style.display = "none";
                        };
                    }

                    // Ẩn overlay khi toast đóng
                    toastEl.addEventListener('hidden.bs.toast', function () {
                        if (toastOverlay) {
                            toastOverlay.style.display = "none";
                        }
                    });

                    bsToast.show();

                    document.getElementById("contactForm").reset();
                }
            })
            .catch(err => console.error("Lỗi:", err));
    });
