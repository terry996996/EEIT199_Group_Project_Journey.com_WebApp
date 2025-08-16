<template>
  <div class="checkout-page">
    <Header />

    <div class="container my-5 pt-4">
      <div class="row">
        <!-- 結帳表單 -->
        <div class="col-md-8">
          <div class="card mb-4">
            <div class="card-body">
              <form @submit.prevent="handleSubmit">
                <div class="d-flex justify-content-between align-items-center mb-4">
                  <div v-if="dataLoaded" class="text-success">
                    <i class="bi bi-check-circle-fill me-1"></i>
                    <small>已自動載入您的資料</small>
                  </div>
                </div>
                <div class="mb-4">
                  <label class="form-label">
                    <h4>選擇旅程</h4>
                  </label>
                  <select id="trip" class="form-select" v-model="form.tripId" required>
                    <option :value="null" selected disabled v-if="form.tripId === null" class="secondary">請選擇欲加入的旅程
                    </option>
                    <option v-for="plan in sortedPlans" :key="plan.id" :value="plan.id">
                      {{ plan.title }}
                    </option>
                  </select>
                </div>

                <h4 class="card-title mb-3">付款人聯絡資訊</h4>
                <!-- 航班 & 景點票聯絡人資訊（中文姓名） -->
                <div class="mb-4" v-if="order.type !== 'hotel'">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">姓氏</label>
                      <input type="text" class="form-control" v-model="form.familyName" required />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">名字</label>
                      <input type="text" class="form-control" v-model="form.givenName" required />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">電話</label>
                      <input type="tel" class="form-control" v-model="form.phone" required />
                    </div>
                    <div class="col-6">
                      <label class="form-label">Email</label>
                      <input type="email" class="form-control" v-model="form.email" required />
                    </div>
                  </div>
                </div>

                <!-- 旅館聯絡人資訊（英文姓名） -->
                <div class="mb-4" v-if="order.type === 'hotel'">
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">姓氏 (以英文填寫)</label>
                      <input type="text" class="form-control" v-model="form.familyNameLatin" required
                        placeholder="英文姓(例:WANG)" @input="form.familyNameLatin = form.familyNameLatin.toUpperCase()" />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">名字 (以英文填寫)</label>
                      <input type="text" class="form-control" v-model="form.givenNameLatin" required
                        placeholder="英文名(例:DATUNG)" @input="form.givenNameLatin = form.givenNameLatin.toUpperCase()" />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">電話</label>
                      <input type="tel" class="form-control" v-model="form.phone" required />
                    </div>
                    <div class="col-6">
                      <label class="form-label">Email</label>
                      <input type="email" class="form-control" v-model="form.email" required />
                    </div>
                    <div class="col-md-6">
                      <label class="form-label">國籍<span style="color: red; margin-left: 2px;">*</span></label>
                      <select class="form-select" v-model="form.nationality" required>
                        <option value="" disabled>－請選擇－</option>
                        <option v-for="nation in counrtyStore.countries" :key="nation.id" :value="nation.id">
                          {{ nation.nameZh }}
                        </option>
                      </select>
                    </div>
                  </div>
                </div>

                <!-- 乘客資訊 (機票限定) -->
                <div class="mb-4" v-if="order.type === 'flight'">
                  <div class="d-flex justify-content-between align-items-center mb-3">
                    <h4 class="mb-0">乘客資訊</h4>
                    <div v-if="dataLoaded && form.passengers.length > 0" class="text-success">
                      <i class="bi bi-check-circle-fill me-1"></i>
                      <small>第一個成人乘客已自動填入您的資料</small>
                    </div>
                  </div>
                  <div v-for="(passenger, index) in form.passengers" :key="index" class="card mb-3">
                    <div class="card-body">

                      <h6 class="fw-bold mb-4">
                        <span class="border-start border-4 border-success ps-2">
                          旅客{{ index + 1 }}（{{ getPassengerTypeLabel(passenger.type) }}）
                        </span>
                      </h6>
                      <div class="row g-3">
                        <div class="col-md-3">
                          <label class="form-label">姓氏<span style="color: red; margin-left: 2px;">*</span></label>
                          <input type="text" class="form-control" v-model="passenger.lastName" required
                            placeholder="英文姓(例:WANG)" @input="passenger.lastName = passenger.lastName.toUpperCase()" />
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">名字<span style="color: red; margin-left: 2px;">*</span></label>
                          <input type="text" class="form-control" v-model="passenger.firstName" required
                            placeholder="英文名(例:DATUNG)"
                            @input="passenger.firstName = passenger.firstName.toUpperCase()" />
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">性別<span style="color: red; margin-left: 2px;">*</span></label>
                          <select class="form-select" v-model="passenger.gender" required>
                            <option value="" disabled>－請選擇－</option>
                            <option value="male">男</option>
                            <option value="female">女</option>
                          </select>
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">出生日期<span style="color: red; margin-left: 2px;">*</span></label>
                          <input type="date" class="form-control" v-model="passenger.birthday"
                            :class="{ 'is-invalid': !validatePassengerAge(passenger) }" required />
                          <div class="invalid-feedback" v-if="!validatePassengerAge(passenger)">
                            出生日期與乘客類型不符，請重新確認。
                          </div>
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">國籍<span style="color: red; margin-left: 2px;">*</span></label>
                          <select class="form-select" v-model="passenger.nationality" required>
                            <option value="" disabled>－請選擇－</option>
                            <option v-for="nation in counrtyStore.countries" :key="nation.id" :value="nation.id">
                              {{ nation.nameZh }}
                            </option>
                          </select>
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">護照號碼<span style="color: red; margin-left: 2px;">*</span></label>
                          <input type="text" class="form-control" v-model="passenger.passportNumber" required />
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">護照有效期限<span style="color: red; margin-left: 2px;">*</span></label>
                          <!-- <input type="date" class="form-control" v-model="passenger.passportExpiry" required /> -->
                          <input type="date" class="form-control" v-model="passenger.passportExpiry"
                            :class="{ 'is-invalid': !validatePassportExpiry(passenger) }" required />
                          <div class="invalid-feedback" v-if="!validatePassportExpiry(passenger)">
                            護照已過期，請換新後再訂購。
                          </div>
                        </div>
                        <div class="col-md-3">
                          <label class="form-label">乘客票價
                            <span v-if="passenger.type === 'adult'">(× 1)</span>
                            <span v-else-if="passenger.type === 'child'">(× 0.75)</span>
                            <span v-else-if="passenger.type === 'infant'">(× 0.1)</span>
                          </label>
                          <div class="form-control-plaintext">NT$ {{ formatCurrency(calculatePassengerPrice(passenger))
                          }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 航班資訊 -->
                <!-- 去程航班 (機票限定) -->
                <div class="mb-4" v-if="order.type === 'flight' && outboundFlight">
                  <h5 class="mb-3">去程航班資訊</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">航班號碼</label>
                          <div>{{ outboundFlight.flightNumber }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">飛行時間</label>
                          <div>{{ formatDuration(outboundFlight.duration) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發機場</label>
                          <div>{{ outboundFlight.departureAirport }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發時間</label>
                          <div>{{ formatDateTime(outboundFlight.departureTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達機場</label>
                          <div>{{ outboundFlight.arrivalAirport }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達時間</label>
                          <div>{{ formatDateTime(outboundFlight.arrivalTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">行李限重</label>
                          <div>{{ outboundFlight.baggageAllowance }}kg</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">基本票價</label>
                          <div>NT$ {{ outboundFlight.classPrice.toLocaleString() }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 回程航班 (機票限定) -->
                <div class="mb-4" v-if="order.type === 'flight' && returnFlight">
                  <h5 class="mb-3">回程航班資訊</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">航班號碼</label>
                          <div>{{ returnFlight.flightNumber }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">飛行時間</label>
                          <div>{{ formatDuration(returnFlight.duration) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發機場</label>
                          <div>{{ returnFlight.departureAirport }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發時間</label>
                          <div>{{ formatDateTime(returnFlight.departureTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達機場</label>
                          <div>{{ returnFlight.arrivalAirport }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達時間</label>
                          <div>{{ formatDateTime(returnFlight.arrivalTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">行李限重</label>
                          <div>{{ returnFlight.baggageAllowance }}kg</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">基本票價</label>
                          <div>NT$ {{ returnFlight.classPrice.toLocaleString() }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!-- 多程航班 (機票限定) -->
                <div class="mb-4" v-if="order.type === 'flight' && multiFlights.length > 0">
                  <h5 class="mb-3">多行程航班資訊</h5>
                  <div v-for="(flight, index) in multiFlights" :key="index" class="card mb-3">
                    <div class="card-body">
                      <h6 class="mb-3">第 {{ index + 1 }} 段</h6>
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">航班號碼</label>
                          <div>{{ flight.flightNumber }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">飛行時間</label>
                          <div>{{ formatDuration(flight.duration) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發機場</label>
                          <div>
                            {{ flight.departureAirport }}
                          </div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">出發時間</label>
                          <div>{{ formatDateTime(flight.departureTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達機場</label>
                          <div>
                            {{ flight.arrivalAirport }}
                          </div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">抵達時間</label>
                          <div>{{ formatDateTime(flight.arrivalTime) }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">行李限重</label>
                          <div>{{ flight.baggageAllowance }}kg</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">基本票價</label>
                          <div>NT$ {{ flight.classPrice.toLocaleString() }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>


                <!-- 住宿資訊 (旅館限定) -->
                <div class="mb-4" v-if="order.type === 'hotel'">
                  <h5 class="mb-3">住宿資訊</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-12">
                          <label class="form-label fw-bold">飯店名稱</label>
                          <div class="form-control-plaintext">{{ order.items[0].hotelName }} - {{
                            order.items[0].roomName }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">入住日期</label>
                          <div class="form-control-plaintext">{{ order.items[0].checkInDate }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">退房日期</label>
                          <div class="form-control-plaintext">{{ order.items[0].checkOutDate }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">入住人數</label>
                          <div class="form-control-plaintext">{{ order.items[0].guestCount }}人</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">每晚價格</label>
                          <div class="form-control-plaintext">NT$ {{ order.items[0].pricePerNight }}</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>


                <!-- --------(景點票限定) --------------------------->
                <div class="mb-4" v-if="order.type === 'ticket'">
                  <h5 class="mb-3">票券資訊</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">票券名稱</label>
                          <div class="form-control-plaintext">{{ order.title }}</div>
                        </div>
                        <div class="col-md-6">
                          <label class="form-label">使用日期</label>
                          <div class="form-control-plaintext">{{ order.date }}</div>
                        </div>

                        <div class="col-md-6" v-for="(item, index) in order.items" :key="index">

                          <div class="form-control-plaintext">
                            {{ item.name }} x {{ item.quantity }} 張<br />
                            單價：NT$ {{ item.price.toLocaleString() }}
                          </div>
                        </div>

                        <div class="col-md-6">
                          <label class="form-label">總金額</label>
                          <div class="form-control-plaintext text-primary fw-bold">
                            NT$ {{ order.subtotal.toLocaleString() }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <!--------------------------------------------景點票結束-------------------------------->


                <!-- (交通票限定) -->
                <div class="mb-4" v-if="order.type === 'traffic'">
                  <h5 class="mb-3">限定只有交通票才能顯示的部分</h5>
                  <div class="card mb-3">
                    <div class="card-body">
                      <div class="row g-3">
                        <div class="col-md-6">
                          <label class="form-label">要顯示的欄位和樣式自己設定</label>
                          <div>要顯示的欄位和樣式自己設定</div>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>


                <!-- 付款方式 -->
                <div class="mb-4">
                  <h5 class="mb-3">付款方式</h5>
                  <div class="payment-methods">
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="creditCard"
                        value="creditCard" v-model="form.paymentMethod" />
                      <label class="form-check-label" for="creditCard">
                        <i class="bi bi-credit-card me-2"></i>
                        信用卡付款
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="linePay" value="linePay"
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="linePay">
                        <i class="bi bi-wallet2 me-2"></i>
                        LINE Pay
                      </label>
                    </div>
                    <div class="form-check mb-2">
                      <input class="form-check-input" type="radio" name="paymentMethod" id="transfer" value="transfer"
                        v-model="form.paymentMethod" />
                      <label class="form-check-label" for="transfer">
                        <i class="bi bi-bank me-2"></i>
                        銀行轉帳
                      </label>
                    </div>
                  </div>
                </div>

                <!-- 發票資訊 -->
                <div class="mb-4">
                  <h5 class="mb-3">發票資訊</h5>
                  <div class="row g-3">
                    <div class="col-md-6">
                      <label class="form-label">發票類型</label>
                      <select class="form-select" v-model="form.invoiceType">
                        <option value="personal">個人發票</option>
                        <option value="company">公司發票</option>
                        <option value="donation">捐贈發票</option>
                      </select>
                    </div>
                    <div class="col-md-6" v-if="form.invoiceType === 'company'">
                      <label class="form-label">統一編號</label>
                      <input type="text" class="form-control" v-model="form.taxId" />
                    </div>
                    <div class="col-12" v-if="form.invoiceType === 'donation'">
                      <label class="form-label">捐贈單位</label>
                      <select class="form-select" v-model="form.donationUnit">
                        <option value="1">創世基金會</option>
                        <option value="2">家扶基金會</option>
                        <option value="3">陽光基金會</option>
                      </select>
                    </div>
                  </div>
                </div>

                <div class="d-grid">
                  <button type="submit" class="btn btn-primary btn-lg">確認訂單並前往付款</button>
                </div>
              </form>
            </div>
          </div>
        </div>

        <!-- 訂單摘要 -->
        <div class="col-md-4">
          <div class="card booking-summary sticky-top" style="top: 2rem">
            <div class="card-body">
              <h5 class="card-title mb-4">訂單摘要</h5>

              <!-- 折價券選擇 -->
              <div class="coupon-section mb-4">
                <h6 class="mb-3 d-flex justify-content-between align-items-center">
                  <span>折價券</span>
                  <button type="button" class="btn btn-outline-secondary btn-sm ms-auto me-2" v-if="appliedCoupon"
                    @click="clearCoupon">
                    清除折價券
                  </button>
                  <button @click="toggleCouponsVisibility" class="btn btn-sm btn-outline-secondary"
                    :disabled="!showCoupons && sortedAvailableCoupons.length <= 3">
                    {{ showCoupons ? '收攏折價券' : '展開折價券' }}
                  </button>
                </h6>
                <transition name="fade">
                  <div>
                    <div v-if="displayedCoupons.length > 0" class="coupon-list">
                      <div v-for="coupon in displayedCoupons" :key="coupon.id" class="coupon-item mb-2">
                        <div class="form-check">
                          <input class="form-check-input" type="radio" :id="'coupon-' + coupon.id" :value="coupon.id"
                            v-model="selectedCouponId" @change="handleCouponSelection(coupon)" />
                          <label class="form-check-label" :for="'coupon-' + coupon.id">
                            <div class="coupon-info">
                              <div class="coupon-title">{{ coupon.title }}</div>
                              <div class="coupon-desc">
                                {{ coupon.description }}
                              </div>
                              <div class="coupon-validity">
                                <small class="text-muted">
                                  到期日：{{ formatDate(coupon.validUntil) }}
                                </small>
                              </div>
                            </div>
                          </label>
                        </div>
                      </div>
                    </div>
                    <div v-else class="text-muted">
                      <small>暫無可用折價券</small>
                    </div>
                  </div>
                </transition>
              </div>
              <!-- 訂單內容 -->
              <div class="order-details mb-4">
                <div class="d-flex align-items-center mb-3" v-if="order.type !== 'flight'">
                  <img :src="order.image" class="me-3" style="width: 60px; height: 60px; object-fit: cover"
                    :alt="order.title" />
                  <div>
                    <h6 class="mb-1">{{ order.title }}</h6>
                    <div class="text-muted">{{ order.date }}</div>
                  </div>
                </div>

                <!-- 訂單項目 -->
                <div class="order-items">
                  <div class="flight-detail-list mb-4"
                    v-if="order.type === 'flight' && order.items && order.items.length > 0">
                    <h6 class="mb-2">航段詳情</h6>
                    <div v-for="(item, index) in order.items" :key="index" class="border rounded p-2 mb-2 bg-light">
                      <div class="fw-bold mb-1">
                        第 {{ index + 1 }} 段：{{ item.departureAirport }} → {{ item.arrivalAirport }}
                      </div>
                      <div class="text-muted small">
                        航空公司：{{ item.airline }}<br />
                        航班號碼：{{ item.flightNumber }}<br />
                        艙等：{{ item.cabinClass || '經濟艙' }}<br />
                        起飛時間：{{ formatDateTime(item.departureTime) }}<br />
                        抵達時間：{{ formatDateTime(item.arrivalTime) }}
                      </div>
                    </div>
                  </div>
                  <div class="flight-summary mb-4"
                    v-if="order.type === 'flight' && form.passengers && form.passengers.length > 0">
                    <div class="flight-ticket-item d-flex justify-content-between mb-2" v-if="adultCount > 0">
                      <span>成人機票總額 × {{ adultCount }}</span>
                      <span>NT$ {{ formatCurrency(adultCount * baseFlightPrice) }}</span>
                    </div>
                    <div class="flight-ticket-item d-flex justify-content-between mb-2" v-if="childCount > 0">
                      <span>孩童機票總額 × {{ childCount }}</span>
                      <span>NT$ {{ formatCurrency(childCount * baseFlightPrice * 0.75) }}</span>
                    </div>
                    <div class="flight-ticket-item d-flex justify-content-between mb-2" v-if="infantCount > 0">
                      <span>嬰兒機票總額 × {{ infantCount }}</span>
                      <span>NT$ {{ formatCurrency(infantCount * baseFlightPrice * 0.1) }}</span>
                    </div>
                  </div>

                </div>
              </div>
              <!-- 價格明細 -->
              <div class="price-details">
                <div class="d-flex justify-content-between mb-2">
                  <span>小計</span>
                  <span>NT$ {{ formatCurrency(order.subtotal) }}</span>
                </div>
                <hr />
                <div class="d-flex justify-content-between mb-2" v-if="appliedCoupon">
                  <span>折價券折扣</span>
                  <span class="text-danger">-NT$ {{ appliedCoupon.discountAmount.toLocaleString() }}</span>
                </div>
                <div class="d-flex justify-content-between mb-2" v-if="order.tax">
                  <span>稅金</span>
                  <span>NT$ {{ order.tax }}</span>
                </div>
                <div class="d-flex justify-content-between fw-bold">
                  <span>總計</span>
                  <span class="text-primary">NT$ {{ formatCurrency(calculateTotal()) }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
  <!-- </div> -->
</template>

<script setup>
  import { ref, onMounted, watch, computed } from 'vue';
  import { useRouter } from 'vue-router';
  import { useRoute } from 'vue-router';
  import Header from '../components/Header.vue';
  import dayjs from 'dayjs';
  import isSameOrAfter from 'dayjs/plugin/isSameOrAfter';
  dayjs.extend(isSameOrAfter);
  import { jwtDecode } from 'jwt-decode';
  import axios from 'axios';
  import { useCountriesStore } from '@/stores/countries';
  import { useOrderStore } from '@/stores/orderStore'//景點
  import { usePlansStore } from '@/stores/plans';

  const planStore = usePlansStore();
  const plans = computed(() => planStore.plans);
  const sortedPlans = computed(() => {
    return plans.value.slice().sort((a, b) => b.startDate - a.startDate);
  })

  const route = useRoute();
  const router = useRouter();
  const selectedFlight = ref(null); // 選擇的航班
  const outboundFlight = ref(null); // 去程
  const returnFlight = ref(null); // 回程
  const multiFlights = ref([]); // 多程航班
  const orderStore = useOrderStore()//景點票
  // 初始化 countries store
  const counrtyStore = useCountriesStore();

  // 表單資料
  const form = ref({
    // 中文姓名（航班 & 景點票使用）
    familyName: '',
    givenName: '',
    // 英文姓名（旅館使用）
    familyNameLatin: '',
    givenNameLatin: '',
    phone: '',
    email: '',
    nationality: '',
    passengers: [
      {
        name: '',
        idNumber: '',
        birthday: '',
        type: 'adult',
      }
    ],
    paymentMethod: 'creditCard',
    cardNumber: '',
    cardExpiry: '',
    cardCVC: '',
    invoiceType: 'personal',
    taxId: '',
    donationUnit: '',
    tripId: null,
  });

  // 資料載入狀態
  const dataLoaded = ref(false);

  // 折價券相關
  const selectedCouponId = ref(null);
  const appliedCoupon = ref(null);
  // 控制折價券收攏/展開
  const showCoupons = ref(false); // 預設折價券是收攏的

  // 清空選擇之折價券的方法
  const clearCoupon = () => {
    selectedCouponId.value = null; // 將選定的折價券 ID 清空
    appliedCoupon.value = null; // 將已套用的折價券物件清空
    console.log('已清除選定的折價券。');
  };

  // 可用的折價券 (現在將從後端抓取真實資料)
  const availableCoupons = ref([]);

  // 排序後的可用折價券
  const sortedAvailableCoupons = computed(() => {
    if (!availableCoupons.value || availableCoupons.value.length === 0) {
      return [];
    }
    // 過濾掉 isZeroDiscount 為 true 的折價券，然後再排序
    return [...availableCoupons.value]
      .filter((coupon) => !coupon.isZeroDiscount) // <--- 新增的過濾條件
      .sort((a, b) => {
        // 處理 validUntil 為 null 的情況：將 null 排在後面 (表示永不過期)
        if (a.validUntil === null && b.validUntil !== null) return 1;
        if (a.validUntil !== null && b.validUntil === null) return -1;
        if (a.validUntil === null && b.validUntil === null) return 0;

        // 將日期字串轉換為 Date 物件進行比較
        const dateA = new Date(a.validUntil);
        const dateB = new Date(b.validUntil);

        // 依到期日由近到遠排序 (升序)
        return dateA.getTime() - dateB.getTime();
      });
  });

  // 實際顯示的折價券（控制數量）
  const displayedCoupons = computed(() => {
    if (showCoupons.value) {
      // 展開狀態：顯示所有排序後的折價券
      return sortedAvailableCoupons.value;
    } else {
      // 收攏狀態：顯示排序後的「前三張」折價券
      return sortedAvailableCoupons.value.slice(0, 3);
    }
  });

  // 切換折價券顯示狀態
  const toggleCouponsVisibility = () => {
    showCoupons.value = !showCoupons.value;
  };

  // 模擬訂單資料 先寫死的訂單資料 (預設如果沒有按其他旅程相關的東西，這裡的資料會顯示在畫面右邊)
  const order = ref({
    image: 'image/location/disney.webp',
    title: '東京迪士尼樂園一日門票',
    date: '2024-04-01',
    type: 'ticket', // 訂單類型
    tripId: null,
    items: [
      {
        id: 1,
        name: '成人票',
        quantity: 2,
        price: 2500
      },
      {
        id: 2,
        name: '兒童票',
        quantity: 1,
        price: 2000
      }
    ],
    subtotal: 7000,
    discount: 500,
    tax: 0,
    total: 6500
  });

  // 載入旅程數據
  const loadJourneyData = () => {
    try {
      const checkoutData = JSON.parse(localStorage.getItem('checkoutData') || 'null');
      if (checkoutData && checkoutData.items && checkoutData.items.length > 0) {
        // 使用旅程數據更新訂單
        const firstItem = checkoutData.items[0];
        order.value = {
          image: firstItem.image,
          title: `${firstItem.name} 等 ${checkoutData.totalItems} 項商品`,
          date: new Date().toISOString().split('T')[0],
          type: firstItem.type || 'mixed',
          tripId: checkoutData.tripId || null,
          items: checkoutData.items.map((item) => ({
            id: item.id,
            name: item.name,
            quantity: item.quantity || 1,
            price: item.price
          })),
          subtotal: checkoutData.totalPrice,
          discount: 0, // 重置折扣，讓折價券重新計算
          tax: 0,
          total: checkoutData.totalPrice
        };
        console.log('loadJourneyData: order.value.subtotal 已更新為', order.value.subtotal);
        console.log('loadJourneyData: order.value.type 已更新為', order.value.type); // 新增日誌
      } else {
        console.log(
          'loadJourneyData: 未從 localStorage 載入數據，使用預設 subtotal:',
          order.value.subtotal
        );
        console.log('loadJourneyData: 使用預設 order.value.type:', order.value.type); // 新增日誌
      }
    } catch (error) {
      console.error('Error loading journey data:', error);
    }
  };

  // 從後端獲取可用折價券的函數
  const fetchUsableCoupons = async () => {
    console.log('fetchUsableCoupons: 當前 order.value.subtotal:', order.value.subtotal);
    console.log('fetchUsableCoupons: 當前 order.value.type:', order.value.type); // 新增：顯示訂單類型

    try {
      const response = await fetch(`http://localhost:8080/coupons/usable-coupons`);
      if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
      }
      const data = await response.json();
      console.log('fetchUsableCoupons: 從後端獲取原始折價券數據:', data);

      // Filter coupons based on minSpend, expireTime, and applicableProductTypes
      const filteredData = data.filter((coupon) => {
        // 1. 檢查最低消費門檻 (minSpend)
        const isMinSpendMet = !coupon.minSpend || coupon.minSpend <= order.value.subtotal;

        // 2. 檢查是否過期 (expireTime)
        // 如果 coupon.expireTime 不存在，表示永不過期；否則判斷是否在當前時間之後
        const isNotExpired = !coupon.expireTime || new Date(coupon.expireTime) > new Date();

        // 3. 檢查適用商品類型 (applicableProductTypes)
        // 如果 applicableProductTypes 不存在或為空，表示適用於所有商品類型
        // 否則，判斷當前訂單類型 (order.value.type) 是否包含在 applicableProductTypes 中
        const isProductTypeApplicable =
          !coupon.applicableProductTypes ||
          coupon.applicableProductTypes.length === 0 ||
          (order.value.type && coupon.applicableProductTypes.includes(order.value.type));

        console.log(
          `   - Coupon: ${coupon.couponName}, MinSpendMet: ${isMinSpendMet}, NotExpired: ${isNotExpired}, ProductTypeApplicable: ${isProductTypeApplicable}`
        );

        return isMinSpendMet && isNotExpired && isProductTypeApplicable;
      });

      console.log('fetchUsableCoupons: 篩選後的折價券數據:', filteredData);

      // 對每一張篩選後的折價券，呼叫後端 API 進行實際折扣計算
      const processedCoupons = [];
      for (const coupon of filteredData) {
        let isZeroDiscountActual = false;
        let discountAmount = 0; // 初始化折扣金額

        try {
          const calculateResponse = await fetch(
            'http://localhost:8080/coupons/calculate-discount',
            {
              method: 'POST',
              headers: {
                'Content-Type': 'application/json'
              },
              body: JSON.stringify({
                couponInstanceId: coupon.couponInstanceId,
                orderSubtotal: order.value.subtotal
              })
            }
          );

          if (!calculateResponse.ok) {
            // 如果計算折扣失敗（例如後端錯誤），為了避免阻擋用戶，我們假設它不是零折扣券
            console.warn(
              `計算折價券 "${coupon.couponName}" 折扣失敗，將預設為可用:`,
              await calculateResponse.text()
            );
            isZeroDiscountActual = false;
          } else {
            discountAmount = await calculateResponse.json();
            if (discountAmount === 0) {
              isZeroDiscountActual = true;
            }
          }
        } catch (error) {
          // 如果呼叫 API 本身就失敗（例如網路問題），也假設它不是零折扣券
          console.error(
            `呼叫 calculate-discount API 失敗 for coupon "${coupon.couponName}":`,
            error
          );
          isZeroDiscountActual = false;
        }

        const mappedCoupon = {
          id: coupon.couponInstanceId,
          title: coupon.couponName,
          minAmount: coupon.minSpend,
          description:
            (coupon.minSpend && coupon.minSpend > 0
              ? `滿 NT$ ${coupon.minSpend.toLocaleString()} `
              : '') +
            (coupon.type === 'fixed'
              ? `折抵 NT$ ${coupon.value.toLocaleString()}`
              : coupon.type === 'percentage'
                ? `享 ${coupon.value}% 折扣`
                : ''),
          validUntil: coupon.expireTime,
          applicableProducts: coupon.applicableProductTypes || [],
          isZeroDiscount: isZeroDiscountActual // 使用後端實際計算的折扣來判斷是否為零折扣
        };
        processedCoupons.push(mappedCoupon);

        // 增加詳細日誌輸出，方便確認 isZeroDiscount 狀態
        console.log(`處理折價券 "${mappedCoupon.title}" (ID: ${mappedCoupon.id}):`);
        console.log(`  - 類型: ${coupon.type}, 原始數值: ${coupon.value}`);
        console.log(`  - 後端實際計算折扣: ${discountAmount}`);
        console.log(`  - 是否為零折扣 (isZeroDiscount): ${mappedCoupon.isZeroDiscount}`);
      }

      availableCoupons.value = processedCoupons;
      console.log(
        'fetchUsableCoupons: 最終 availableCoupons.value (已根據後端實際折扣判斷):',
        availableCoupons.value
      );
    } catch (error) {
      console.error('獲取可用折價券失敗:', error);
      availableCoupons.value = [];
    }
  };

  // 格式化日期
  const formatDate = (dateString) => {
    if (!dateString) return '無到期日';
    const date = new Date(dateString);
    return new Date(date.getTime() + date.getTimezoneOffset() * 60000).toLocaleDateString('zh-TW');
  };

  // 處理折價券選取變更的函數
  const handleCouponSelection = async (selectedCoupon) => {
    // 在這裡，由於 fetchUsableCoupons 已經進行了實際折扣判斷，
    // 這裡的 isZeroDiscount 已經是準確的了。
    // 如果使用者嘗試選擇一個已經被過濾掉的（isZeroDiscount 為 true）折價券，
    // 理論上不會發生，因為它不會被顯示。
    if (selectedCoupon.isZeroDiscount) {
      alert(`折價券 "${selectedCoupon.title}" 在當前訂單條件下無折扣效果，無法選取。`);
      selectedCouponId.value = null;
      appliedCoupon.value = null;
      return;
    }

    if (selectedCoupon) {
      console.log('handleCouponSelection: 選取了折價券:', selectedCoupon.title);
      try {
        const response = await fetch('http://localhost:8080/coupons/calculate-discount', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            couponInstanceId: selectedCoupon.id,
            orderSubtotal: order.value.subtotal
          })
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }

        const discountAmount = await response.json();
        console.log('handleCouponSelection: 後端計算的折扣金額:', discountAmount);

        // 再次確認後端實際計算出的折扣金額為 0 時的處理
        if (discountAmount === 0) {
          selectedCoupon.isZeroDiscount = true; // 動態更新此折價券的狀態，使其從顯示列表中移除
          alert(
            `所選折價券 "${selectedCoupon.title}" 在當前訂單條件下無折扣效果，已自動取消選取。`
          );
          selectedCouponId.value = null; // 取消選取此折價券
          appliedCoupon.value = null;
          // 由於狀態改變了，觸發重新渲染以從顯示列表中移除該項
          return;
        }

        appliedCoupon.value = {
          ...selectedCoupon,
          discountAmount: discountAmount
        };
      } catch (error) {
        console.error('handleCouponSelection: 計算折扣金額失敗:', error);
        alert('折價券無法應用，請稍後再試。');
        selectedCouponId.value = null;
        appliedCoupon.value = null;
      }
    } else {
      console.log('handleCouponSelection: 未選取折價券。');
      appliedCoupon.value = null;
    }
  };

  // 監聽 order.subtotal 的變化，當訂單小計改變時，重新計算折價券折扣
  watch(
    () => order.value.subtotal,
    (newSubtotal, oldSubtotal) => {
      console.log(
        'watch(order.value.subtotal): subtotal 變化 detected! New:',
        newSubtotal,
        'Old:',
        oldSubtotal
      );
      // 重新獲取可用折價券，以便重新判斷其適用性及是否為零折扣
      fetchUsableCoupons();

      if (selectedCouponId.value !== null && newSubtotal !== oldSubtotal) {
        const currentSelectedCoupon = availableCoupons.value.find(
          (coupon) => coupon.id === selectedCouponId.value
        );
        if (currentSelectedCoupon) {
          console.log(
            'watch(order.value.subtotal): 檢查已選折價券門檻 - ',
            currentSelectedCoupon.title,
            'MinSpend:',
            currentSelectedCoupon.minAmount
          );
          // 如果已選取的折價券的 isZeroDiscount 變為 true (現在這個判斷會更精準)，
          // 或者不再符合最低消費門檻，則自動取消選取
          if (
            currentSelectedCoupon.isZeroDiscount ||
            !currentSelectedCoupon.minAmount ||
            newSubtotal < currentSelectedCoupon.minAmount
          ) {
            console.log(
              'watch(order.value.subtotal): 已選折價券不再符合門檻或變成零折扣券，自動取消。'
            );
            selectedCouponId.value = null;
            appliedCoupon.value = null;
            alert(
              `所選折價券 "${currentSelectedCoupon.title}" 因不適用或未達最低消費金額，已自動取消選取。`
            );
          } else {
            // 重新計算已選折價券的折扣，以防折扣金額因小計變動而異
            // 這裡再次呼叫 handleCouponSelection，確保即使小計變化，已選折價券的折扣也是最新的
            handleCouponSelection(currentSelectedCoupon);
          }
        }
      } else if (selectedCouponId.value === null) {
        appliedCoupon.value = null;
      }
    },
    { immediate: true }
  );

  // 計算總計
  const calculateTotal = () => {
    let total = order.value.subtotal;
    if (appliedCoupon.value) {
      total -= appliedCoupon.value.discountAmount;
    }
    if (order.value.discount && !appliedCoupon.value) {
      total -= order.value.discount;
    }
    if (order.value.tax) {
      total += order.value.tax;
    }

    return Math.max(0, total);
  };

  //飛機票價資訊總計
  const calculateTotalFlightPrice = () => {
    let price = 0;

    // 計算每位乘客乘坐每段航班的票價
    form.value.passengers.forEach((passenger) => {
      // 成人、兒童、嬰兒可依不同定價區分，如果目前 classPrice 為成人票價，可以自行加乘比例
      const multiplier =
        passenger.type === 'adult'
          ? 1
          : passenger.type === 'child'
            ? 0.75 // 兒童票價 75%
            : passenger.type === 'infant'
              ? 0.1
              : 1; // 嬰兒票價 10%

      if (outboundFlight.value?.classPrice) {
        price += outboundFlight.value.classPrice * multiplier;
      }
      if (returnFlight.value?.classPrice) {
        price += returnFlight.value.classPrice * multiplier;
      }
      if (multiFlights.value.length > 0) {
        multiFlights.value.forEach((flight) => {
          if (flight.classPrice) {
            price += flight.classPrice * multiplier;
          }
        });
      }
    });

    return Math.round(price);
  };

  // 計算每位乘客的票價
  const calculatePassengerPrice = (passenger) => {
    const multiplier =
      passenger.type === 'adult'
        ? 1
        : passenger.type === 'child'
          ? 0.75
          : passenger.type === 'infant'
            ? 0.1
            : 1;

    let total = 0;

    if (outboundFlight.value?.classPrice) {
      total += outboundFlight.value.classPrice * multiplier;
    }
    if (returnFlight.value?.classPrice) {
      total += returnFlight.value.classPrice * multiplier;
    }
    if (multiFlights.value.length > 0) {
      multiFlights.value.forEach(flight => {
        if (flight.classPrice) {
          total += flight.classPrice * multiplier;
        }
      });
    }

    return Math.round(total);
  };



  // 基本飛行價格 = 所有選取航段的 classPrice 總和（未乘人數）
  const baseFlightPrice = computed(() => {
    let price = 0;
    if (outboundFlight.value?.classPrice) {
      price += outboundFlight.value.classPrice;
    }
    if (returnFlight.value?.classPrice) {
      price += returnFlight.value.classPrice;
    }
    if (multiFlights.value.length > 0) {
      price += multiFlights.value.reduce((sum, f) => sum + (f.classPrice || 0), 0);
    }
    return Math.round(price);
  });

  // 計算每種乘客人數
  const adultCount = computed(() => form.value.passengers.filter((p) => p.type === 'adult').length);
  const childCount = computed(() => form.value.passengers.filter((p) => p.type === 'child').length);
  const infantCount = computed(
    () => form.value.passengers.filter((p) => p.type === 'infant').length
  );

  // 格式化幣值
  const formatCurrency = (value) => {
    return Math.round(value).toLocaleString();
  };


  const getPassengerTypeLabel = (type) => {
    switch (type) {
      case 'adult':
        return '成人';
      case 'child':
        return '孩童';
      case 'infant':
        return '嬰兒';
      default:
        return '未知';
    }
  };

  // 驗證乘客年齡
  const validatePassengerAge = (passenger) => {
    if (!passenger.birthday || !passenger.type) return true;

    const today = dayjs();
    const birthDate = dayjs(passenger.birthday);
    const age = today.diff(birthDate, 'year', true); // 精確到小數

    switch (passenger.type) {
      case 'adult':
        return age >= 12;
      case 'child':
        return age >= 2 && age < 12;
      case 'infant':
        return age < 2;
      default:
        return false;
    }
  };

  // 驗證護照有效期限（不得早於最早出發日期）
  const validatePassportExpiry = (passenger) => {
    if (!passenger.passportExpiry) return true;
    const expiryDate = dayjs(passenger.passportExpiry);
    let earliestDeparture = null;

    if (outboundFlight.value?.departureTime) {
      earliestDeparture = dayjs(outboundFlight.value.departureTime);
    } else if (multiFlights.value.length > 0) {
      earliestDeparture = multiFlights.value
        .map(f => dayjs(f.departureTime))
        .reduce((min, current) => (current.isBefore(min) ? current : min));
    }

    if (!expiryDate.isValid() || !earliestDeparture?.isValid()) return true;

    return expiryDate.isSameOrAfter(earliestDeparture, 'day');
  };



  //----------------------------------------------------到時候後端串接綠界、linepay要用的-----------------------------------------------
  // 處理表單提交 - 修改為呼叫後端 API--------------------------------------------------------------------------------------------------

  // 定義類型名稱與對應後端的type_id值
  const orderTypeToIdMap = {
    hotel: 1,
    flight: 2,
    traffic: 3,
    ticket: 4
  };

  // 處理表單提交 - 修改為呼叫後端 API
  const handleSubmit = async () => {
    console.log('提交訂單：', form.value);
    console.log('使用的折價券：', appliedCoupon.value);
    console.log('最終總計：', calculateTotal());
    console.log('當前 order.value.type:', order.value.type);

    // **將 order.value.type 字串轉換為後端需要的數字 orderTypeId** // 非常重要，後面才能夠根據這個查資料出來，並將訂單、付款資料寫到資料表內
    const orderTypeId = orderTypeToIdMap[order.value.type];

    // 檢查是否找到了對應的數字 ID
    if (typeof orderTypeId === 'undefined' || orderTypeId === null) {
      alert(`無效的訂單類型 "${order.value.type}"，無法處理付款。請確認商品類型是否正確設定。`);
      console.error('未知的訂單類型，無法映射到 orderTypeId:', order.value.type);
      return; // 阻止表單提交
    }

    console.log('轉換後的 orderTypeId:', orderTypeId); // 新增 log 確認轉換結果

    // 準備要傳送給後端的訂單數據 // 這邊一些欄位先寫死 之後要改
    const orderDataForBackend = {
      orderNo: 'ORD' + Date.now().toString().slice(-10) + Math.floor(Math.random() * 100),
      totalAmount: calculateTotal(),
      paymentMethod: form.value.paymentMethod,
      orderTypeId: orderTypeId,
      itemName: order.value.title,
      description: order.value.title,
      tripId: 1, // 我還沒有trip plan的資料和邏輯，這邊先直接寫死
      selectedCouponInstanceId: appliedCoupon.value ? appliedCoupon.value.id : null
    };

    console.log('傳送給後端的資料:', orderDataForBackend); // 再次確認傳送的資料

    // 綠界支付，判斷用戶是選信用卡還是銀行轉帳，會跳往不同頁面

    try {
      if (form.value.paymentMethod === 'creditCard' || form.value.paymentMethod === 'transfer') {
        console.log(
          `選擇 ${form.value.paymentMethod === 'creditCard' ? '信用卡' : '銀行轉帳'
          } 付款，呼叫綠界 API...`
        );
        const response = await fetch('http://localhost:8080/api/ecpay/create-payment', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(orderDataForBackend)
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }

        const ecpayParams = await response.json();
        console.log('從後端接收到綠界支付參數:', ecpayParams);

        const formEl = document.createElement('form');
        formEl.method = 'POST';
        formEl.action = 'https://payment-stage.ecpay.com.tw/Cashier/AioCheckOut/V5';

        // *** 原視窗跳轉 ***

        formEl.style.display = 'none';

        for (const key in ecpayParams) {
          const input = document.createElement('input');
          input.type = 'hidden';
          input.name = key;
          input.value = ecpayParams[key];
          formEl.appendChild(input);
        }

        document.body.appendChild(formEl);
        formEl.submit(); // 提交表單，導向綠界支付頁面
        setTimeout(() => {
          document.body.removeChild(formEl);
        }, 1000); // 先設定久一點，以免太快，後端還沒insert好訂單和付款資料，會報錯

        // LINE Pay 付款：呼叫 LINE Pay 支付 API
      } else if (form.value.paymentMethod === 'linePay') {
        console.log('選擇 LINE Pay 付款，呼叫 LINE Pay API...');
        const response = await fetch('http://localhost:8080/api/linepay/create-payment', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify(orderDataForBackend)
        });

        if (!response.ok) {
          const errorText = await response.text();
          throw new Error(`HTTP error! status: ${response.status}, message: ${errorText}`);
        }

        const linePayResponse = await response.json();
        console.log('從後端接收到 LINE Pay 支付響應:', linePayResponse);

        console.log(
          'LINE Pay webPaymentUrl (info.paymentUrl.webPaymentUrl):',
          linePayResponse.info.paymentUrl?.webPaymentUrl
        );
        console.log(
          'LINE Pay 直接的 webPaymentUrl (info.webPaymentUrl):',
          linePayResponse.info?.webPaymentUrl
        );

        if (
          linePayResponse.returnCode === '0000' &&
          linePayResponse.info &&
          linePayResponse.info.webPaymentUrl
        ) {
          const redirectToUrl = linePayResponse.info.webPaymentUrl;
          console.log('導向 LINE Pay 支付頁面:', redirectToUrl);

          window.location.href = redirectToUrl; // 在原視窗導向
        } else {
          throw new Error(
            `LINE Pay request failed or valid payment URL not found: ${linePayResponse.returnMessage || '未知錯誤'
            }`
          );
        }
      } else {
        console.warn('未知的付款方式:', form.value.paymentMethod);
        alert('請選擇有效的付款方式。');
      }
    } catch (error) {
      console.error('提交訂單失敗:', error);
      alert('訂單提交失敗，請稍後再試。\n錯誤訊息: ' + error.message);
    }


  };

  // 格式化飛行時間為「X時 Y分」
  const formatDuration = (minutes) => {
    if (!minutes && minutes !== 0) return '無資料';
    const hours = Math.floor(minutes / 60);
    const mins = minutes % 60;
    return `${hours}時 ${mins}分`;
  };

  // 格式化日期時間
  const formatDateTime = (dateTimeString) => {
    if (!dateTimeString) return '無資料';
    return dayjs(dateTimeString).format('YYYY-MM-DD HH:mm');
  };


  //------------------------------------------------------------------------------------------------------------------------------------------------------------
  const loadStores = async () => {
    try {
      await Promise.all([
        counrtyStore.getData(),
        planStore.getData(
          jwtDecode(localStorage.getItem('token')).id
        ),
      ])
      console.log("load country, plan stores");
    } catch (error) {
      console.log("load store error: " + error);
    }
  };

  onMounted(async () => {
    // 載入國家資料
    loadStores();
    console.log("planStore.plans" + planStore.plans);

    fetchUsableCoupons();
    loadJourneyData();
    const tripType = route.query.type;
    if (tripType === 'round') {
      const roundTripData = localStorage.getItem('roundTripCheckout');
      if (roundTripData) {
        const parsed = JSON.parse(roundTripData);
        outboundFlight.value = parsed.outbound || null;
        returnFlight.value = parsed.return || null;
      }
      order.value.type = 'flight';
      // 設定來回航班的 order.value
      if (outboundFlight.value && returnFlight.value) {
        const outboundDate = outboundFlight.value.departureTime?.split('T')[0] || '';
        const returnDate = returnFlight.value.departureTime?.split('T')[0] || '';

        order.value = {
          image: 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=400', // 航班圖片
          title: `${outboundFlight.value.departureAirport} ↔ ${outboundFlight.value.arrivalAirport}`,
          date: `${outboundDate} ~ ${returnDate}`,
          type: 'flight',
          tripId: null,
          items: [
            {
              id: 1,
              name: `${outboundFlight.value.airline} ${outboundFlight.value.flightNumber} (去程)`,
              quantity: 1,
              price: outboundFlight.value.classPrice,
              departureAirport: outboundFlight.value.departureAirport,
              arrivalAirport: outboundFlight.value.arrivalAirport,
              departureTime: outboundFlight.value.departureTime,
              arrivalTime: outboundFlight.value.arrivalTime,
              airline: outboundFlight.value.airline,
              flightNumber: outboundFlight.value.flightNumber,
              cabinClass: outboundFlight.value.flightclass
            },
            {
              id: 2,
              name: `${returnFlight.value.airline} ${returnFlight.value.flightNumber} (回程)`,
              quantity: 1,
              price: returnFlight.value.classPrice,
              departureAirport: returnFlight.value.departureAirport,
              arrivalAirport: returnFlight.value.arrivalAirport,
              departureTime: returnFlight.value.departureTime,
              arrivalTime: returnFlight.value.arrivalTime,
              airline: returnFlight.value.airline,
              flightNumber: returnFlight.value.flightNumber,
              cabinClass: returnFlight.value.flightclass
            }
          ],
          subtotal: outboundFlight.value.classPrice + returnFlight.value.classPrice,
          discount: 0,
          tax: 0
        };
      }
    } else if (tripType === 'multi') {
      const multiTripData = localStorage.getItem('multiTripCheckout');
      if (multiTripData) {
        const parsed = JSON.parse(multiTripData);
        multiFlights.value = parsed;
      }
      order.value.type = 'flight';
      // 設定多程航班的 order.value
      if (multiFlights.value.length > 0) {
        const firstFlight = multiFlights.value[0];
        const lastFlight = multiFlights.value[multiFlights.value.length - 1];
        const firstDate = firstFlight.departureTime?.split('T')[0] || '';
        const lastDate = lastFlight.departureTime?.split('T')[0] || '';

        const totalPrice = multiFlights.value.reduce((sum, flight) => sum + (flight.classPrice || 0), 0);

        order.value = {
          image: 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=400', // 航班圖片
          title: `${firstFlight.departureAirport} → ${lastFlight.arrivalAirport} (${multiFlights.value.length}段)`,
          date: `${firstDate} ~ ${lastDate}`,
          type: 'flight',
          tripId: null,
          items: multiFlights.value.map((flight, index) => ({
            id: index + 1,
            name: `${flight.airline} ${flight.flightNumber} (第${index + 1}段)`,
            quantity: 1,
            price: flight.classPrice,
            departureAirport: flight.departureAirport,
            arrivalAirport: flight.arrivalAirport,
            departureTime: flight.departureTime,
            arrivalTime: flight.arrivalTime,
            airline: flight.airline,
            flightNumber: flight.flightNumber,
            cabinClass: flight.flightclass
          })),
          subtotal: totalPrice,
          discount: 0,
          tax: 0
        };
      }
    } else if (tripType === 'one') {
      // 單程航班
      const flightData = localStorage.getItem('checkoutFlight');
      if (flightData) {
        outboundFlight.value = JSON.parse(flightData);
      }
      order.value.type = 'flight';
      // 設定單程航班的 order.value
      if (outboundFlight.value) {
        const departureDate = outboundFlight.value.departureTime?.split('T')[0] || '';

        order.value = {
          image: 'https://images.unsplash.com/photo-1436491865332-7a61a109cc05?w=400', // 航班圖片
          title: `${outboundFlight.value.departureAirport} → ${outboundFlight.value.arrivalAirport}`,
          date: departureDate,
          type: 'flight',
          tripId: null,
          items: [{
            id: 1,
            name: `${outboundFlight.value.airline} ${outboundFlight.value.flightNumber}`,
            quantity: 1,
            price: outboundFlight.value.classPrice,
            departureAirport: outboundFlight.value.departureAirport,
            arrivalAirport: outboundFlight.value.arrivalAirport,
            departureTime: outboundFlight.value.departureTime,
            arrivalTime: outboundFlight.value.arrivalTime,
            airline: outboundFlight.value.airline,
            flightNumber: outboundFlight.value.flightNumber,
            cabinClass: outboundFlight.value.flightclass
          }],
          subtotal: outboundFlight.value.classPrice,
          discount: 0,
          tax: 0
        };
      }
    } else if (tripType === 'hotel') {
      // 旅館
      order.value.type = 'hotel';

      // 載入旅館資料
      const hotelId = route.query.hotelId;
      const roomId = route.query.roomId;
      const checkIn = route.query.checkIn;
      const checkOut = route.query.checkOut;
      const guests = route.query.guests;

      if (hotelId && roomId) {
        try {
          // 從 API 載入旅館詳情
          const response = await axios.get(`http://localhost:8080/api/lodgings/${hotelId}/roomType/${roomId}`);
          const hotelData = response.data;

          // 計算住宿天數
          const checkInDate = dayjs(checkIn);
          const checkOutDate = dayjs(checkOut);
          const nights = checkOutDate.diff(checkInDate, 'day');

          // 更新訂單資料
          order.value = {
            image: hotelData.lodgingImages?.[0]?.imageUrl || '',
            title: `${hotelData.lodgingName}-${hotelData.roomTypeName}`,
            date: `${checkIn} ~ ${checkOut}`,
            type: 'hotel',
            tripId: null,
            items: [{
              id: 1,
              name: `${hotelData.lodgingName} ${hotelData.roomTypeName}`,
              quantity: 1,
              price: hotelData.pricePerNight,
              hotelName: hotelData.lodgingName,
              roomName: hotelData.roomTypeName,
              checkInDate: checkIn,
              checkOutDate: checkOut,
              guestCount: guests,
              pricePerNight: hotelData.pricePerNight
            }],
            subtotal: nights * hotelData.pricePerNight,
            discount: 0,
            tax: 0
          };

          console.log('✅ 已載入旅館資料:', order.value);
        } catch (error) {
          console.error('❌ 載入旅館資料失敗:', error);
          // 如果載入失敗，使用預設資料
          const defaultCheckIn = checkIn || '2024-12-25';
          const defaultCheckOut = checkOut || '2024-12-27';
          const defaultCheckInDate = dayjs(defaultCheckIn);
          const defaultCheckOutDate = dayjs(defaultCheckOut);
          const defaultNights = defaultCheckOutDate.diff(defaultCheckInDate, 'day');

          order.value = {
            image: 'https://images.unsplash.com/photo-1581309638082-877cb8132535',
            title: '旅館預訂',
            date: `${defaultCheckIn} ~ ${defaultCheckOut}`,
            type: 'hotel',
            tripId: null,
            items: [{
              id: 1,
              name: '旅館房間',
              quantity: 1,
              price: 3000,
              hotelName: '示例旅館',
              roomName: '標準房',
              checkInDate: defaultCheckIn,
              checkOutDate: defaultCheckOut,
              guestCount: guests || '2',
              pricePerNight: 3000
            }],
            subtotal: defaultNights * 3000,
            discount: 0,
            tax: 0
          };
        }
      }
    }
    // ===========================================票券開始================================
    //票券
    else if (tripType === 'ticket') {
      // ✅ 票券：從 Pinia 抓資料
      const store = orderStore

      order.value = {

        image: store.ticketImageUrl,
        title: store.ticketName,
        date: store.useDate,
        type: store.type,
        tripId: null,
        items: store.selectedOptions.map((opt, index) => ({
          id: index + 1,
          name: opt.optionName,
          quantity: opt.quantity,
          price: opt.unitPrice
        })),
        subtotal: store.totalPrice,
        discount: 0,
        tax: 0
      }
      console.log('✅ 已載入票券資料:', order.value)
    } else {
      alert('不支援的訂單型態，將返回首頁')
      router.push('/')
    }
    // =============================票券結束=====================================================
    // 智能載入乘客資訊
    const loadPassengerData = () => {
      console.log('🔍 開始載入乘客資訊...');
      const searchFormData = localStorage.getItem('flightSearchForm');
      console.log('🔍 flightSearchForm 資料:', searchFormData);

      if (searchFormData) {
        const parsed = JSON.parse(searchFormData);
        const p = parsed.passengers || {};
        const passengersArray = [];

        // 獲取使用者資料用於自動填入
        const userInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');
        console.log('🔍 從 localStorage 獲取的使用者資料:', userInfo);

        // 創建成人乘客
        for (let i = 0; i < (p.adult || 0); i++) {
          const passenger = {
            lastName: '',
            firstName: '',
            gender: '',
            birthday: '',
            nationality: 'TW',
            passportNumber: '',
            passportExpiry: '',
            type: 'adult'
          };

          // 如果是第一個成人乘客，自動填入使用者資料
          if (i === 0 && userInfo) {
            console.log('🔍 正在自動填入第一個成人乘客資料:', userInfo);

            passenger.lastName = userInfo.familyNameLatin || '';
            passenger.firstName = userInfo.givenNameLatin || '';
            passenger.nationality = userInfo.nationality || 'TW';
            passenger.birthday = userInfo.birthday || '';
            passenger.passportNumber = userInfo.passportNo || '';

            // 轉換護照有效期限格式：從 YYYY-MM 轉換為 YYYY-MM-DD
            if (userInfo.passportExpire) {
              console.log('🔍 原始護照有效期限:', userInfo.passportExpire);
              // 如果是 YYYY-MM 格式，加上 -01 變成 YYYY-MM-01
              if (userInfo.passportExpire.match(/^\d{4}-\d{2}$/)) {
                passenger.passportExpiry = userInfo.passportExpire + '-01';
                console.log('🔍 轉換後的護照有效期限:', passenger.passportExpiry);
              } else {
                passenger.passportExpiry = userInfo.passportExpire;
                console.log('🔍 使用原始護照有效期限:', passenger.passportExpiry);
              }
            } else {
              passenger.passportExpiry = '';
              console.log('🔍 沒有護照有效期限資料');
            }

            passenger.gender = userInfo.gender === 1 ? 'male' : userInfo.gender === 2 ? 'female' : '';

            console.log('🔍 完成填入乘客資料:', passenger);
          }

          passengersArray.push(passenger);
        }

        // 創建兒童乘客
        for (let i = 0; i < (p.child || 0); i++) {
          passengersArray.push({
            lastName: '',
            firstName: '',
            gender: '',
            birthday: '',
            nationality: 'TW',
            passportNumber: '',
            passportExpiry: '',
            type: 'child'
          });
        }

        // 創建嬰兒乘客
        for (let i = 0; i < (p.infant || 0); i++) {
          passengersArray.push({
            lastName: '',
            firstName: '',
            gender: '',
            birthday: '',
            nationality: 'TW',
            passportNumber: '',
            passportExpiry: '',
            type: 'infant'
          });
        }

        if (passengersArray.length > 0) {
          form.value.passengers = passengersArray;
          console.log('✅ 已載入乘客資訊，第一個成人乘客已自動填入使用者資料');
        }
      }
    };

    // 採用 Profile.vue 的方式載入使用者資料
    const loadUserData = async () => {
      try {
        // 優先從 localStorage 載入
        const localUserInfo = JSON.parse(localStorage.getItem('userInfo') || 'null');

        if (localUserInfo && Object.keys(localUserInfo).length > 0) {
          // 中文姓名（航班 & 景點票使用）
          form.value.familyName = localUserInfo.familyName || '';
          form.value.givenName = localUserInfo.givenName || '';
          // 英文姓名（旅館使用）
          form.value.familyNameLatin = localUserInfo.familyNameLatin || '';
          form.value.givenNameLatin = localUserInfo.givenNameLatin || '';
          form.value.phone = localUserInfo.telNumber || '';
          form.value.email = localUserInfo.email || '';
          form.value.nationality = localUserInfo.nationality || 'TW';
          dataLoaded.value = true;
          console.log('✅ 已從 localStorage 載入使用者資料');
        } else {
          // 如果 localStorage 沒有資料，採用 Profile.vue 的方式從後端載入
          const token = localStorage.getItem('token');
          if (token) {
            try {
              const payload = jwtDecode(token);
              const userId = payload.id;
              const api = 'http://localhost:8080/api/profile/';

              console.log('🔄 正在從後端載入使用者資料...');
              const response = await axios.get(api + userId);

              if (response.data) {
                const userData = response.data;

                // 更新 localStorage（採用 Profile.vue 的方式）
                localStorage.setItem('userInfo', JSON.stringify(userData));

                // 填入表單
                form.value.familyName = userData.familyName || '';
                form.value.givenName = userData.givenName || '';
                form.value.familyNameLatin = userData.familyNameLatin || '';
                form.value.givenNameLatin = userData.givenNameLatin || '';
                form.value.phone = userData.telNumber || '';
                form.value.email = userData.email || '';
                form.value.nationality = userData.nationality || 'TW';
                dataLoaded.value = true;

                console.log('✅ 已從後端 API 載入使用者資料並更新 localStorage');
              } else {
                console.warn('⚠️ 後端 API 回應為空，請手動填寫聯絡資訊');
              }
            } catch (tokenError) {
              console.warn('⚠️ Token 解析失敗，請手動填寫聯絡資訊');
            }
          } else {
            console.warn('⚠️ 未找到登入 token，請手動填寫聯絡資訊');
          }
        }
      } catch (error) {
        console.error('❌ 載入使用者資料失敗:', error);
        // 如果載入失敗，顯示提示但不阻擋結帳流程
        console.warn('⚠️ 無法載入使用者資料，請手動填寫聯絡資訊');
      }
    };



    // 執行載入使用者資料
    await loadUserData();

    // 執行載入乘客資訊（在使用者資料載入完成後）
    loadPassengerData();


    if (order.value.type === 'flight') {
      order.value.subtotal = calculateTotalFlightPrice();
      console.log('✅ order.subtotal 已更新為乘客總票價:', order.value.subtotal);
    }




  });
</script>

<style scoped>
  .booking-summary {
    border-radius: 12px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  }

  .payment-methods .form-check-label {
    cursor: pointer;
  }

  /* 折價券樣式 */
  .coupon-section {
    border-top: 1px solid #e9ecef;
    padding-top: 1rem;
  }

  .coupon-item {
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 0.75rem;
    transition: all 0.2s ease;
  }

  .coupon-item:hover {
    border-color: #007bff;
    background-color: #f8f9fa;
  }

  .coupon-item .form-check-input:checked+.form-check-label {
    border-color: #007bff;
    background-color: #e9f5ff;
  }

  .coupon-title {
    font-weight: 600;
    color: #007bff;
  }
</style>