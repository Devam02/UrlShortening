function shortenUrl() {
    const url = document.getElementById("urlInput").value;
    const expiry = document.getElementById("expiryInput").value;
  
    if (!url || !expiry) {
      alert("Please enter both URL and expiry time");
      return;
    }
  
    fetch("http://localhost:8080/api/shorten", {
      method: "POST",
      headers: {
        "Content-Type": "application/json"
      },
      body: JSON.stringify({ url: url, expiry: parseInt(expiry) })
    })
    .then(response => response.json())
    .then(data => {
      document.getElementById("result").innerHTML = `Shortened URL: <a href="${data.shortUrl}" target="_blank">${data.shortUrl}</a>`;
    })
    .catch(err => {
      document.getElementById("result").innerText = "Error shortening URL!";
      console.error(err);
    });
  }
  