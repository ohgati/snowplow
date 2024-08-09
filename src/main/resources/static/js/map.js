var kakaoCRS = new L.Proj.CRS("EPSG:5181","+proj=tmerc +lat_0=38 +lon_0=127 +k=1 +x_0=200000 +y_0=500000 +ellps=GRS80 +towgs84=0,0,0,0,0,0,0 +units=m +no_defs",{
    resolutions: [2048, 1024, 512, 256, 128, 64, 32, 16, 8, 4, 2, 1, .5, .25],
    origin: [-3e4, -6e4],
    bounds: [[-3e4 - 4 * Math.pow(2, 19), -6e4], [5 * Math.pow(2, 19) - 3e4, 5 * Math.pow(2, 19) - 6e4]]
})

var map = L.map('map', {
    crs : kakaoCRS,
    center: [37.481072, 126.882343], // 초기 중심 좌표
    zoom: 14, // 초기 줌 레벨 14 = zoom 의 0~13까지의 길이
    zoomControl: false
});

L.tileLayer('http://map.daumcdn.net/map_k3f_prod/bakery/image_map_png/PNG01/v26_gxuw0/{z}/{y}/{x}.png',
    {
        attribution: '&copy; <a href="#">Kakao Map</a> contributors', // 화면 오른쪽 하단 attributors
        maxZoom: 13, // 최대 줌
        minZoom: 0, // 최소 줌
        zoomReverse: true,
        zoomOffset: 1,
        subdomains: "0123",
        continuousWorld: true,
        tms: true
    }
).addTo(map);

function setTerm(index, obj) {
    $("#searchNav li").removeClass("active");
    $(obj).addClass("active");
    alert(index)
}

let linePath= [];
let polyLine = [];
let bounds;
let timeline = []

function searchSnowplow() {
    let stDate = $('#stDate').val();
    let edDate = $('#edDate').val();

    $.ajax({
        url: '/searchSnowPlow',
        type: 'POST',
        data: {
            stDate: stDate,
            edDate: edDate
        },
        dataType : "JSON",
        success: function(response) {


            $('#search-result ul').remove()
            let busInfo = response.data.BusInfo
            let busTime = response.data.BusTime
            let html = '<ul>'

            for (let i = 0; i < busTime.length; i++) {
                let busRunTime = busTime[i].COLL_TIME_ED-busTime[i].COLL_TIME_ST
                let time_hhmmss = convertSecondsToHHMMSS(busRunTime);
                html += '<li class="busList" onclick="getBusRoute(this, '+ busTime[i].B_ID+', '+ busTime[i].C_DATE+')">'
                html += '<p class="list-item"><span class="list-title">차량 번호 : </span>'+busTime[i].B_ID+'</span>'
                html += '<p class="list-item"><span class="list-title">운행 시간 : </span>'+time_hhmmss+'</p>'
                html += '<p class="list-item"><span class="list-title">운행 날짜 : </span>'+busTime[i].C_DATE+'</p>'
                if (busInfo[i].TYPE === 'E1') {
                    html += '<p class="list-item"><span class="list-title">운행 상태 : </span> 운행 중</p>'
                } else {
                    html += '<p class="list-item"><span class="list-title">운행 상태 : </span> 운행 종료</p>'
                }
                html += '</li>'
            }

            html += '</ul>'

            $('#search-result').append(html)
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}

// 시간 변형 함수
function convertSecondsToHHMMSS(seconds) {
    const hours = Math.floor(seconds / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = Math.floor(seconds % 60);

    // HHMMSS 형식으로 반환
    return `${String(hours).padStart(2, '0')}시간 ${String(minutes).padStart(2, '0')}분 ${String(secs).padStart(2, '0')}초`;
}

function getBusRoute(obj, busId, stDate) {
    $(".busList").removeClass("selected")
    $(obj).addClass("selected")

    $.ajax({
        url: '/searchRoute',
        type: 'POST',
        data: {
            stDate: stDate,
            busId: busId,
        },
        dataType : "JSON",
        success: function(response) {
            for (let i = 0; i < polyLine.length; i++) {
                polyLine[i].remove()
            }

            linePath = []
            polyLine = []

            for (let i = 0; i < response.data.length; i++) {
                let lat = response.data[i].LATITUDE
                let lon = response.data[i].LONGITUDE

                timeline.push(response.data[i].COLL_TIME)
                linePath.push([lat, lon]);
            }

            L.marker(linePath[0]).addTo(map)
            L.marker(linePath[linePath.length-1]).addTo(map)
            drawRoute()
        },
        error: function (error) {
            console.error('Error:', error);
        }
    });
}

function drawRoute() {
    let polyline1 = L.polyline(linePath, {
        weight: 10,
        color: '#2D31D5', // 초록색
        opacity: 1,
        dashArray: null
    }).addTo(map);

    let polyline2 = L.polyline(linePath, {
        weight: 6,
        color: '#6495ED', // 초록색
        opacity: 1,
        dashArray: true
    }).addTo(map);

    var decorator = L.polylineDecorator(polyline2, {
        patterns: [
            {
                offset: 0, // 화살표의 위치
                repeat: '20px', // 화살표 반복 여부
                symbol: L.Symbol.arrowHead({
                    pixelSize: 5, // 화살표 크기
                    polygon: false,
                    pathOptions: {stroke: true, color: 'white'}
                })
            }
        ]
    }).addTo(map);

    polyLine.push(polyline1);
    polyLine.push(polyline2);
    polyLine.push(decorator);
    mapBounds()
}

function mapBounds() {
    bounds = L.latLngBounds(linePath);
    map.fitBounds(linePath);
}

