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

// Auto scroll video cards - infinite seamless loop
    document.addEventListener('DOMContentLoaded', function () {
        const scrollContainer = document.getElementById('videoScroll');
        const wrapper = document.getElementById('videoWrapper');
        if (!scrollContainer || !wrapper) return;

        // Clone 2 lần để đảm bảo luôn có nội dung hiển thị
        const originalItems = Array.from(scrollContainer.children);
        originalItems.forEach(item => scrollContainer.appendChild(item.cloneNode(true)));
        originalItems.forEach(item => scrollContainer.appendChild(item.cloneNode(true)));

        let scrollAmount = 0;
        let singleSetWidth = scrollContainer.scrollWidth / 3;
        let scrollSpeed = 0.8;
        let isPaused = false;

        function autoScroll() {
            if (!isPaused) {
                scrollAmount += scrollSpeed;
                // Reset không giật - khi đến set thứ 2, nhảy về set 1
                if (scrollAmount >= singleSetWidth) {
                    scrollAmount -= singleSetWidth;
                }
                scrollContainer.style.transform = `translateX(${-scrollAmount}px)`;
            }
            requestAnimationFrame(autoScroll);
        }

        autoScroll();

        wrapper.addEventListener('mouseenter', () => isPaused = true);
        wrapper.addEventListener('mouseleave', () => isPaused = false);

        scrollContainer.querySelectorAll('iframe').forEach(iframe => {
            iframe.addEventListener('mouseenter', () => isPaused = true);
            iframe.addEventListener('mouseleave', () => isPaused = false);
            iframe.addEventListener('click', () => isPaused = true);
        });

        window.addEventListener('resize', () => {
            singleSetWidth = scrollContainer.scrollWidth / 3;
        });
    });



