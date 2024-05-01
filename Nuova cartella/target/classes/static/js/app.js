// Funzione per il caricamento del file CSV
function uploadCSV() {
    const input = document.getElementById('fileInput');
    const data = new FormData();
    data.append('file', input.files[0]);

    fetch('/upload_csv', {
        method: 'POST',
        body: data
    })
    .then(response => response.text())
    .then(text => {
                           document.getElementById('csvUploadResult').innerText = text;
                           // Call fetchValidationResults only if upload was successful
                           if (text.includes("successfully")) { // Adjust this condition based on actual success message
                               fetchValidationResults();
                           }
                       })
    .catch(err => document.getElementById('csvUploadResult').innerText = 'Error uploading file: ' + err.message);
}

function validateAndSavePhoneNumber() {
    const phoneNumber = document.getElementById('phoneNumberInput').value;
    fetch('/controllAndSave/' + encodeURIComponent(phoneNumber), {
        method: 'GET'
    })
    .then(response => {
        console.log('Response received:', response);
        if (response.ok) {
            return response.json();
        } else {
            response.text().then(text => {
                console.error('Failed to fetch data:', text);
                throw new Error('Failed to validate: ' + text);
            });
        }
    })
    .then(data => {
        console.log('Data:', data);
        if (data && data.length > 0) {
                const firstResult = data[0];
                const phoneNumber = firstResult.phoneNumber || 'N/A'; // Usa 'N/A' se il phoneNumber è vuoto
                const status = firstResult.status || 'N/A'; // Usa 'N/A' se lo status è vuoto
            const table = `<table>
                             <tr>
                               <th>Phone Number</th>
                               <th>Status</th>
                             </tr>
                             <tr>
                               <td>${phoneNumber}</td>
                               <td>${status}</td>
                             </tr>
                           </table>`;
            document.getElementById('phoneNumberResult').innerHTML = table;
        } else {
            document.getElementById('phoneNumberResult').innerText = 'No valid data returned';
        }
    })
    .catch(error => {
        console.error('Error:', error);
        document.getElementById('phoneNumberResult').innerText = 'Error: ' + error.message;
    });
}

    // Function to fetch and display validation results
    function fetchValidationResults() {
        fetch('/validate')
        .then(response => response.json())
        .then(data => {
            displayResults(data);
        })
        .catch(err => console.error('Error fetching validation results:', err));
    }

    // Function to display the results in tabular format
  function displayResults(data) {
      let html = '<h3>Validation Results</h3><table><thead><tr>';

      // Adding headers for all columns
      if (data.acceptableNumbers && data.acceptableNumbers.length > 0) {
          html += '<th>Acceptable Numbers</th>';
      }
      if (data.correctedNumbers && Object.keys(data.correctedNumbers).length > 0) {
          html += '<th>Original Number</th><th>Corrected Number</th>';
      }
      if (data.incorrectNumbers && data.incorrectNumbers.length > 0) {
          html += '<th>Invalid Numbers</th>';
      }

      html += '</tr></thead><tbody><tr>';

      // Adding data for all columns
      if (data.acceptableNumbers && data.acceptableNumbers.length > 0) {
          html += `<td>${data.acceptableNumbers.join('<br>')}</td>`;
      }
      if (data.correctedNumbers && Object.keys(data.correctedNumbers).length > 0) {
          html += '<td>' + Object.keys(data.correctedNumbers).join('<br>') + '</td>' +
                  '<td>' + Object.values(data.correctedNumbers).join('<br>') + '</td>';
      }
      if (data.incorrectNumbers && data.incorrectNumbers.length > 0) {
          html += `<td>${data.incorrectNumbers.join('<br>')}</td>`;
      }

      html += '</tr></tbody></table>';
      document.getElementById('phoneNumberResult').innerHTML = html;
  }


    // Helper function to generate HTML table for given title and data
    function generateTable(title, data) {
        let rows = data.map(entry => {
            if (Array.isArray(entry)) {
                return `<tr><td>${entry[0]}</td><td>${entry[1]}</td></tr>`;
            }
            return `<tr><td>${entry}</td></tr>`;
        }).join('');

        let headers = Array.isArray(data[0]) ? '<th>Original Number</th><th>Corrected Number</th>' : '<th>Number</th>';

        return `
        <div class="table-wrapper">
            <h4>${title}</h4>
            <table>
                <thead>
                    <tr>${headers}</tr>
                </thead>
                <tbody>
                    ${rows}
                </tbody>
            </table>
        </div>
        `;
    }



