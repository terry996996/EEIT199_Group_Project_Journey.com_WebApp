import axios from "axios";
import { defineStore } from "pinia";

export const useCountriesStore = defineStore('countries', {
    state: () => ({
        countries: [],
        loading: false,
    }),

    actions: {
        getData() {
            if (this.countries.length) {
                return Promise.resolve();
            } else {
                this.loading = true;
                const api = "http://localhost:8080/api/locations";
                return axios.get(api + "/getCountries")
                    .then(response => {
                        this.countries = response.data;
                    })
                    .finally(() => {
                        this.loading = false;
                    })
            }
        }
    }
})