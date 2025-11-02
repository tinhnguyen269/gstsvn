// Tăng giá trị Flex
document.addEventListener("DOMContentLoaded", () => {
    const counters = document.querySelectorAll(".stat-box h3");
    const section = document.querySelector(".stats-section");
    let started = false; // đảm bảo chỉ chạy 1 lần

    function animateCounter(counter) {
        const target = +counter.innerText.replace(/\D/g, ''); // lấy số từ nội dung
        let current = 0;
        const duration = 3000; // thời gian chạy (ms)
        const stepTime = Math.max(10, duration / target);

        const timer = setInterval(() => {
            current += Math.ceil(target / (duration / stepTime));
            if (current >= target) {
                counter.innerText = counter.innerText.includes('%')
                    ? target + '%'
                    : target.toLocaleString('vi-VN');
                clearInterval(timer);
            } else {
                counter.innerText = counter.innerText.includes('%')
                    ? current + '%'
                    : current.toLocaleString('vi-VN');
            }
        }, stepTime);
    }

    function handleScroll() {
        const rect = section.getBoundingClientRect();
        const windowHeight = window.innerHeight;

        if (rect.top < windowHeight && !started) {
            counters.forEach(animateCounter);
            started = true;
        }
    }

    window.addEventListener("scroll", handleScroll);
});

// Xử lý gửi form liên hệ với Fetch API và CSRF
    document.getElementById("contactForm").addEventListener("submit", function (e) {
        // Lấy CSRF token và header name từ meta
        const csrfToken = document.querySelector('meta[name="_csrf"]').content;
        const csrfHeader = document.querySelector('meta[name="_csrf_header"]').content;

        e.preventDefault();

        // Xóa lỗi cũ
        document.querySelectorAll(".text-danger").forEach(el => el.innerText = "");

        let formData = new FormData(this);

        fetch("/lien-he/them-moi", {
            method: "POST",
            body: formData,
            headers: {
                [csrfHeader]: csrfToken // Gửi CSRF token
            }
        })
            .then(res => res.json())
            .then(data => {
                if (data[1].status === "error") {
                    for (let field in data[1].errors[1]) {
                        let errDiv = document.getElementById("error-" + field);
                        if (errDiv) {
                            errDiv.innerText = data[1].errors[1][field];
                        }
                    }
                }
                if (data[1].status === "success") {
                    let toastEl = document.getElementById("successToast");
                    let toastBody = toastEl.querySelector(".toast-body");
                    toastBody.innerText = data[1].message;

                    let bsToast = new bootstrap.Toast(toastEl, {delay: 2500});
                    bsToast.show();

                    document.getElementById("contactForm").reset();
                }
            })
            .catch(err => console.error("Lỗi:", err));
    });

// Auto scroll video cards
    document.addEventListener('DOMContentLoaded', function () {
        const scrollContainer = document.getElementById('videoScroll');
        const wrapper = document.getElementById('videoWrapper');

        let scrollAmount = 0;
        let maxScroll = scrollContainer.scrollWidth - wrapper.clientWidth;
        let scrollSpeed = 1; // pixels per frame approx
        let requestId;
        let isPaused = false;

        function autoScroll() {
            if (!isPaused) {
                scrollAmount += scrollSpeed;
                if (scrollAmount >= maxScroll) {
                    scrollAmount = 0; // reset scroll về đầu
                }
                scrollContainer.style.transform = `translateX(${-scrollAmount}px)`;
            }
            requestId = requestAnimationFrame(autoScroll);
        }

        // Bật auto scroll
        autoScroll();

        // Pause khi hover vào wrapper hoặc video card
        wrapper.addEventListener('mouseenter', () => isPaused = true);
        wrapper.addEventListener('mouseleave', () => isPaused = false);

        // Pause khi click vào video (iframe)
        scrollContainer.querySelectorAll('iframe').forEach(iframe => {
            iframe.addEventListener('mouseenter', () => isPaused = true);
            iframe.addEventListener('mouseleave', () => isPaused = false);
            // Nếu cần pause khi click
            iframe.addEventListener('click', () => {
                isPaused = true;
            });
        });

        // Khi cửa sổ thay đổi kích thước, cập nhật maxScroll lại
        window.addEventListener('resize', () => {
            maxScroll = scrollContainer.scrollWidth - wrapper.clientWidth;
        });
    });



