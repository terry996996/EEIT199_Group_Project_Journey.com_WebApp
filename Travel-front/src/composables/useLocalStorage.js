import { ref, watch } from 'vue';
import localStorageManager from '@/utils/localStorageUtils';

export function useLocalStorage(key, defaultValue = null) {
  // 創建響應式變數
  const data = ref(localStorageManager.getItem(key) || defaultValue);

  // 監聽 localStorage 變化
  localStorageManager.addListener(key, (newValue) => {
    data.value = newValue || defaultValue;
  });

  // 設置資料的函數
  const setData = (value) => {
    localStorageManager.setItem(key, value);
  };

  // 移除資料的函數
  const removeData = () => {
    localStorageManager.removeItem(key);
  };

  return {
    data,
    setData,
    removeData
  };
}
