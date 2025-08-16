import axios from "axios";
import { defineStore } from "pinia";

const api = "http://localhost:8080/api/partners/"

export const usePartnersStore = defineStore('partners', {
    state: () => ({
        partners: [],
        loaded: false,
    }),

    actions: {
        async getData(userId) {
            if (this.loaded) {
                return;
            }

            try {
                const response = await axios.get(api + userId);
                const data = response.data;
                this.partners = data;
                this.loaded = true;
            } catch (error) {
                console.error(error.response?.data || error.message);
            }

        },

        async addNewPartner(profile, userId) {
            try {
                const response = await axios.post(api + "add/" + userId, profile);
                const dto = response.data;
                profile.lastModified = dto.lastModified;
                const newPartner = {
                    id: dto.id,
                    profile: profile,
                    deleteStatus: 1,
                };
                this.partners.push(newPartner);
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },

        async updatePartner(partnerId, proflie) {
            try {
                const response = await axios.put(api + "update/" + partnerId, proflie, { responseType: 'text' });
                if (response.data) {
                    const index = this.partners.findIndex(partner => partner.id === partnerId);
                    this.partners[index].proflie = proflie;
                    this.partners[index].profile.lastModified = response.data;
                }
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },

        async removePartner(partnerId) {
            try {
                const response = await axios.delete(api + partnerId);
                if (response.data) {
                    const index = this.partners.findIndex(partner => partner.id === partnerId);
                    this.partners[index].deleteStatus = 0;
                    this.partners[index].profile.deleteTime = response.data.deleteTime;
                    this.partners[index].profile.lastModified = response.data.lastModified;
                }
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },

        async recoverPartner(partnerId) {
            try {
                const response = await axios.put(api + "recover/" + partnerId, { responseType: 'text' });
                if (response.data) {
                    const index = this.partners.findIndex(partner => partner.id === partnerId);
                    this.partners[index].deleteStatus = 1;
                    this.partners[index].profile.lastModified = response.data;
                }
            } catch (error) {
                console.error(error.response?.data || error.message);
            }
        },

    }


});