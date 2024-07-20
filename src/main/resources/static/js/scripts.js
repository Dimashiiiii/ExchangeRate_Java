document.addEventListener('DOMContentLoaded', function() {
    const form = document.getElementById('converter-form');
    const amountInput = document.getElementById('amount');
    const fromCurrencySelect = document.getElementById('from');
    const toCurrencySelect = document.getElementById('to');
    const resultDiv = document.getElementById('conversion-result');

    form.addEventListener('submit', function(event) {
        event.preventDefault();
        const amount = parseFloat(amountInput.value);
        const fromCurrency = fromCurrencySelect.value;
        const toCurrency = toCurrencySelect.value;

        if (isNaN(amount)) {
            resultDiv.innerHTML = '<p>Please enter a valid amount.</p>';
            return;
        }

        fetch(`/api/v1/exchange_rates/convert?amount=${amount}&from=${fromCurrency}&to=${toCurrency}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok ' + response.statusText);
                }
                return response.json();
            })
            .then(data => {
                if (data.success) {
                    resultDiv.innerHTML = `<p>${amount} ${fromCurrency} = ${data.convertedAmount} ${toCurrency}</p>`;
                } else {
                    resultDiv.innerHTML = `<p>Error: ${data.message}. Please try again.</p>`;
                }
            })
            .catch(error => {
                console.error('There was a problem with the fetch operation:', error);
                resultDiv.innerHTML = '<p>Error converting currency. Please try again later.</p>';
            });
    });
});
