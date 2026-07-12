console.log("🚀 Страница about загружена!");

// Ждем полной загрузки DOM
document.addEventListener('DOMContentLoaded', function() {
    const button = document.getElementById('magicButton');
    const message = document.getElementById('magicMessage');

    // Список вдохновляющих цитат
    const quotes = [
        "🌟 'Код — это поэзия.' — Автор неизвестен",
        "💡 'Программирование — это искусство решения проблем.'",
        "🚀 'Spring Boot делает разработку быстрой и приятной.'",
        "✨ 'Лучший код — это тот, который работает.'",
        "📚 'Каждая ошибка — это шаг к успеху.'",
        "🎯 'Цель программиста — писать код, который легко читать.'",
        "💻 'Java — это не язык, это философия.'",
        "🌈 'Сделай это просто, но не проще.'"
    ];

    let quoteIndex = 0;

    // Обработчик клика по кнопке
    button.addEventListener('click', function() {
        // Показываем случайную цитату
        const randomIndex = Math.floor(Math.random() * quotes.length);
        const quote = quotes[randomIndex];

        message.textContent = quote;
        message.classList.add('show');

        // Меняем цвет кнопки для веселья
        const colors = ['#667eea', '#ff6b6b', '#20c997', '#f093fb', '#4facfe'];
        const randomColor = colors[Math.floor(Math.random() * colors.length)];
        button.style.background = `linear-gradient(135deg, ${randomColor}, ${randomColor}dd)`;

        // Счетчик нажатий
        const clickCount = parseInt(button.dataset.clicks || 0) + 1;
        button.dataset.clicks = clickCount;

        if (clickCount > 5) {
            message.textContent = "🔥 Вы настоящий фанат кнопок! Цитата: " + quote;
        }

        // Анимация кнопки
        button.style.transform = 'scale(0.95)';
        setTimeout(() => {
            button.style.transform = 'scale(1)';
        }, 150);
    });

    // Добавляем эффект при загрузке страницы
    const container = document.querySelector('.container');
    container.style.animation = 'fadeInUp 0.8s ease';

    // Логируем информацию о странице
    console.log("📋 Количество цитат:", quotes.length);
    console.log("💻 Привет из JavaScript!");
});
