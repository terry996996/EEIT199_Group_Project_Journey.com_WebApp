import { createApp } from "vue";
import App from "./App.vue";
import router from "./router";
import { createAuth0 } from "@auth0/auth0-vue";
import "./assets/styles.css";
import "bootstrap/dist/css/bootstrap.min.css";
import "bootstrap/dist/js/bootstrap.bundle.min.js";
import "bootstrap-icons/font/bootstrap-icons.css";
import { createPinia } from "pinia";

const initializeData = () => {
    const hotels = [
        {
            id: 1,
            name: "台北君悅酒店",
            location: "台北市信義區",
            rating: 4.8,
            price: 8000,
            image:
                "https://plus.unsplash.com/premium_photo-1661964071015-d97428970584?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MXx8aG90ZWx8ZW58MHx8MHx8fDA%3D",
        },
        {
            id: 2,
            name: "台北W飯店",
            location: "台北市信義區",
            rating: 4.7,
            price: 7500,
            image:
                "https://images.unsplash.com/photo-1618773928121-c32242e63f39?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8aG90ZWx8ZW58MHx8MHx8fDA%3D",
        },
        {
            id: 3,
            name: "台中日月千禧酒店",
            location: "台中市西屯區",
            rating: 4.6,
            price: 5000,
            image:
                "https://images.unsplash.com/photo-1455587734955-081b22074882?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8NHx8aG90ZWx8ZW58MHx8MHx8fDA%3D",
        },
        {
            id: 4,
            name: "高雄漢來大飯店",
            location: "高雄市前金區",
            rating: 4.5,
            price: 4500,
            image:
                "https://images.unsplash.com/photo-1520250497591-112f2f40a3f4?w=900&auto=format&fit=crop&q=60&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxzZWFyY2h8MTB8fGhvdGVsfGVufDB8fDB8fHww",
        },
    ];
    const flights = [
        {
            id: 1,
            airline: "中華航空",
            flightNumber: "CI101",
            departure: "桃園 (TPE)",
            destination: "東京 (NRT)",
            price: 9500,
            image:
                "https://www.wiadvance.com/upload/fac_m/ALL_fac_24A22_8xLaCKyrLx.jpg",
        },
        {
            id: 2,
            airline: "長榮航空",
            flightNumber: "BR183",
            departure: "桃園 (TPE)",
            destination: "大阪 (KIX)",
            price: 8800,
            image:
                "https://www.colorencyc.com/_next/image?url=https%3A%2F%2Fcolor-matching.s3.ap-northeast-1.amazonaws.com%2Fcolor-image%2F%25E9%2595%25B7%25E6%25A6%25AE%25E8%2588%25AA%25E7%25A9%25BA%25EF%25BC%258C%25E6%25A8%2599%25E8%25AA%258C%25EF%25BC%25BB2013%25E5%25B9%25B4%25EF%25BC%258D%25EF%25BC%25BD.jpg&w=1080&q=100",
        },
        {
            id: 3,
            airline: "國泰航空",
            flightNumber: "CX400",
            departure: "桃園 (TPE)",
            destination: "香港 (HKG)",
            price: 6200,
            image:
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRhrj0msM6wKNEGm79JZIHgKopB8pJuA3yaTQ&s",
        },
    ];
    const tickets = [
        {
            id: 1,
            name: "東京迪士尼樂園門票",
            location: "日本東京",
            price: 2200,
            image:
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTAswpW5U-o_41-yRBv0uPLQ-XSECDxq8qYvQ&s",
        },
        {
            id: 2,
            name: "大阪環球影城門票",
            location: "日本大阪",
            price: 2000,
            image:
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQdNYwkciaj8X44eJOCe0z1syqOn25CSltA8g&s",
        },
        {
            id: 3,
            name: "台北101觀景台門票",
            location: "台灣台北",
            price: 600,
            image:
                "https://blog.accupass.com/wp-content/uploads/2023/06/Cover-Tiny_taipei101-observation-deck-ACCUPI-1.png",
        },
    ];
    const trafficTickets = [
        {
            id: 1,
            name: "台灣高鐵票",
            route: "台北 - 高雄",
            price: 1490,
            image:
                "https://cdn.ttv.com.tw/manasystem/FileData/News/59be1fec-3bc2-4202-b961-9467f6d1902c.jpg",
        },
        {
            id: 2,
            name: "台北捷運一日券",
            route: "台北市區",
            price: 150,
            image:
                "https://img.technews.tw/wp-content/uploads/2019/07/01161429/Taipei-MRT_C381_20130908-e1561968902884.jpg",
        },
        {
            id: 3,
            name: "清境農場共乘",
            route: "台中 - 清境",
            price: 500,
            image:
                "https://res.klook.com/images/fl_lossy.progressive,q_65/c_fill,w_1200,h_630/w_80,x_15,y_15,g_south_west,l_Klook_water_br_trans_yhcmh3/activities/apjwcuh9ilf9unz3oaa3/%E3%80%90%E5%85%B1%E4%B9%97%E4%BA%A4%E9%80%9A%E3%80%91%E5%A5%A7%E8%90%AC%E5%A4%A7%E5%9C%8B%E5%AE%B6%E6%A3%AE%E6%9E%97%E5%9C%92%E5%8D%80%20-%20%E6%B8%85%E5%A2%83%E8%BE%B2%E5%A0%B4.jpg",
        },
    ];

    localStorage.setItem("hotelsData", JSON.stringify(hotels));
    localStorage.setItem("flightsData", JSON.stringify(flights));
    localStorage.setItem("ticketsData", JSON.stringify(tickets));
    localStorage.setItem("trafficData", JSON.stringify(trafficTickets));
};

initializeData();

console.log("main.js: Vue App initialization started.");
const app = createApp(App);
const pinia = createPinia();

// Auth0 配置
app.use(
    createAuth0({
        domain: "YOUR_AUTH0_DOMAIN", // 例如：dev-xxx.us.auth0.com
        clientId: "YOUR_AUTH0_CLIENT_ID",
        authorizationParams: {
            redirect_uri: window.location.origin,
            audience: "YOUR_API_IDENTIFIER",
            scope: "openid profile email",
        },
    })
);

app.use(router);
app.use(pinia);
app.mount("#app");
