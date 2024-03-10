fetch('http://localhost:8080/technician')
    .then(response => response.json())
    .then(data => {
        const technicians = data.content;

        technicians.forEach(technician => {

            const imagePath = JSON.parse(technician.image).filePath;

            const imageUrl = 'http://localhost:8080/img/technician/' + imagePath;

            const technicianItem = document.createElement('div');
            technicianItem.classList.add('col-sm-6', 'col-lg-3');

            technicianItem.innerHTML = `
                <div class="single_blog_item">
                    <div class="single_blog_img">
                        <img id="technicianImage" src="${imageUrl}" alt="${technician.name}">
                        <div class="social_icon">
                            <ul>
                                <li><a href="#"> <i class="ti-facebook"></i> </a></li>
                                <li><a href="#"> <i class="ti-twitter-alt"></i> </a></li>
                                <li><a href="#"> <i class="ti-instagram"></i> </a></li>
                                <li><a href="#"> <i class="ti-skype"></i> </a></li>
                            </ul>
                        </div>
                    </div>
                    <div class="single_text">
                        <div class="single_blog_text">
                            <h3>${technician.name}</h3>
                            <p>${technician.position}</p>
                        </div>
                    </div>
                </div>
            `;
            document.getElementById('technicianList').appendChild(technicianItem);
        });
    })
    .catch(error => {
        console.error('Error fetching technician data:', error);
    });