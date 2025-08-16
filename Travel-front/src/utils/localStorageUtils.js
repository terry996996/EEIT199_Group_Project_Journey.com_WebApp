// localStorage 工具函數
class LocalStorageManager {
  constructor() {
    this.listeners = new Map();
  }

  // 設置資料並觸發事件
  setItem(key, value) {
    localStorage.setItem(key, JSON.stringify(value));
    this.triggerEvent(key, value);
  }

  // 獲取資料
  getItem(key) {
    const item = localStorage.getItem(key);
    return item ? JSON.parse(item) : null;
  }

  // 移除資料並觸發事件
  removeItem(key) {
    localStorage.removeItem(key);
    this.triggerEvent(key, null);
  }

  // 添加監聽器
  addListener(key, callback) {
    if (!this.listeners.has(key)) {
      this.listeners.set(key, []);
    }
    this.listeners.get(key).push(callback);
  }

  // 移除監聽器
  removeListener(key, callback) {
    if (this.listeners.has(key)) {
      const callbacks = this.listeners.get(key);
      const index = callbacks.indexOf(callback);
      if (index > -1) {
        callbacks.splice(index, 1);
      }
    }
  }

  // 觸發事件
  triggerEvent(key, value) {
    if (this.listeners.has(key)) {
      this.listeners.get(key).forEach((callback) => {
        callback(value);
      });
    }
  }
}

// 創建全域實例
const localStorageManager = new LocalStorageManager();



export default localStorageManager;
