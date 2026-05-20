// Simple Countdown Timer Logic
function startTimer(duration, display, payButton) {
    let timer = duration, minutes, seconds;
    const interval = setInterval(function () {
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.textContent = minutes + ":" + seconds;

        if (--timer < 0) {
            clearInterval(interval);
            display.textContent = "00:00";
            display.classList.add('text-error', 'animate-pulse');
            
            // Business rule: Disable payment button and notify user
            if (payButton) {
                payButton.disabled = true;
                payButton.classList.add('opacity-50', 'cursor-not-allowed');
                payButton.innerHTML = 'Hết thời gian thanh toán';
            }

            alert("Thanh toán đã quá thời gian cho phép");
            
            // Navigate back to dashboard
            window.location.href = '/passenger/dashboard';
        }
    }, 1000);
}

window.onload = function () {
    // 10 minutes countdown (600 seconds)
    const tenMinutes = 60 * 10;
    const display = document.querySelector('#countdown');
    const payButton = document.querySelector('#btn-pay');
    
    if (display) {
        startTimer(tenMinutes, display, payButton);
    }

    // Payment Method Selection Logic
    const paymentLabels = document.querySelectorAll('#payment-methods label');
    const cardForm = document.getElementById('card-form');

    paymentLabels.forEach(label => {
        label.addEventListener('click', function() {
            // Reset all styles
            paymentLabels.forEach(l => {
                l.classList.remove('border-on-tertiary-container', 'bg-surface-container-low', 'ring-1', 'ring-on-tertiary-container');
                l.classList.add('border-outline-variant');
                
                const icon = l.querySelector('span.material-symbols-outlined:last-child');
                if(icon) {
                    icon.textContent = 'radio_button_unchecked';
                    icon.classList.replace('text-on-tertiary-container', 'text-transparent');
                    icon.classList.add('group-hover:text-outline-variant');
                }
            });

            // Apply active styles to clicked
            this.classList.remove('border-outline-variant');
            this.classList.add('border-on-tertiary-container', 'bg-surface-container-low', 'ring-1', 'ring-on-tertiary-container');

            const radio = this.querySelector('input[type="radio"]');
            if (radio) {
                radio.checked = true;
                
                // Toggle card form visibility based on selection
                if (cardForm) {
                    if (radio.value === 'card') {
                        cardForm.classList.remove('hidden');
                    } else {
                        cardForm.classList.add('hidden');
                    }
                }
            }

            const icon = this.querySelector('span.material-symbols-outlined:last-child');
            if(icon) {
                icon.textContent = 'check_circle';
                icon.classList.replace('text-transparent', 'text-on-tertiary-container');
                icon.classList.remove('group-hover:text-outline-variant');
            }
        });
    });
};
