import { defineStore } from 'pinia'

interface TicketOption {
  optionId: number
  optionName: string
  unitPrice: number
  quantity: number
}

interface OrderState {
    type: string  // ← ✅ 新增這一行
    ticketId: number | null
    ticketName: string
    ticketImageUrl: string
    packageId: number | null
    packageName: string
    selectedOptions: TicketOption[]
    totalPrice: number
    contactName: string
    contactEmail: string
    contactPhone: string
    note: string
    useDate: string
  }
  
  export const useOrderStore = defineStore('order', {
    state: (): OrderState => ({
      type: '',  // ← ✅ 初始為空字串
      ticketId: null,
      ticketName: '',
      ticketImageUrl: '',
      packageId: null,
      packageName: '',
      selectedOptions: [],
      totalPrice: 0,
      contactName: '',
      contactEmail: '',
      contactPhone: '',
      note: '',
      useDate: ''
    }),
    actions: {
      setOrderData(payload: {
        type: string  // ← ✅ 加上 type
        ticketId: number
        ticketName: string
        ticketImageUrl: string
        packageId: number
        packageName: string
        selectedOptions: TicketOption[]
        useDate: string
      }) {
        this.type = payload.type  // ← ✅ 設定 type
        this.ticketId = payload.ticketId
        this.ticketName = payload.ticketName
        this.ticketImageUrl = payload.ticketImageUrl
        this.packageId = payload.packageId
        this.packageName = payload.packageName
        this.selectedOptions = payload.selectedOptions
        this.useDate = payload.useDate
        this.totalPrice = payload.selectedOptions.reduce(
          (sum, opt) => sum + opt.unitPrice * opt.quantity,
          0
        )
      }
    }
  })
  