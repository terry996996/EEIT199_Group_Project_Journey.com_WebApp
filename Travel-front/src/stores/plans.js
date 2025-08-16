import axios from "axios";
import { defineStore } from "pinia";

const api = "http://localhost:8080/api/plans/"

export const usePlansStore = defineStore('plans', {
    state: () => ({
        plans: [],
        nowAtIndex: null,
        loaded: false,
    }),

    actions: {

        async getData(userId) {
            if (this.loaded) {
                return;
            }
            try {
                const response = await axios.get(api + 'getData/' + userId);
                const data = response.data;
                this.plans = data;
                this.loaded = true;
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },

        async addNewPlan(form, userId) {
            try {
                const response = await axios.post(api + "add/" + userId, form);
                const dto = response.data;
                this.plans.push(dto);
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },


    },

})