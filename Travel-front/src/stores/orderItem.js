import { defineStore } from "pinia";

export const UseOrderItemStore = defineStore('orderItem', {
    state: () => ({
        orderType: null,
        details: {},
    }),

    actions: {

        bookingOrder(hotel) {
            this.orderType = 1;
            this.details = {
                lodgingId: hotel.lodgingId,
                roomTypeId: hotel.roomTypeId,
                pricePerNight: hotel.pricePerNight,
                roomTypeName: hotel.roomTypeName,
                lodgingName: hotel.lodgingName,
                maxGuests: hotel.maxGuests,
            }
        },
    }
})