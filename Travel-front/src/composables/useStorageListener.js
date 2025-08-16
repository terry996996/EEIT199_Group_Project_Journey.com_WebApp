import { ref, onMounted, onUnmounted } from 'vue';

export function useStorageListener(key, defaultValue = null) {
  const data = ref(JSON.parse(localStorage.getItem(key) || 'null') || defaultValue);

  const handleStorageChange = (event) => {
    if (event.key === key) {
      data.value = JSON.parse(event.newValue || 'null') || defaultValue;
    }
  };

  // 自定義事件監聽器，用於同頁面內的變化
  const handleCustomStorageChange = (event) => {
    if (event.detail.key === key) {
      data.value = event.detail.value || defaultValue;
    }
  };

  onMounted(() => {
    // 監聽瀏覽器的 storage 事件（跨頁面）
    window.addEventListener('storage', handleStorageChange);
    // 監聽自定義事件（同頁面）
    window.addEventListener('localStorageChange', handleCustomStorageChange);
  });

  onUnmounted(() => {
    window.removeEventListener('storage', handleStorageChange);
    window.removeEventListener('localStorageChange', handleCustomStorageChange);
  });

  const setData = (value) => {
    localStorage.setItem(key, JSON.stringify(value));
    data.value = value;

    // 觸發自定義事件，通知同頁面的其他組件
    window.dispatchEvent(
      new CustomEvent('localStorageChange', {
        detail: { key, value }
      })
    );
  };

  const removeData = () => {
    localStorage.removeItem(key);
    data.value = defaultValue;

    // 觸發自定義事件
    window.dispatchEvent(
      new CustomEvent('localStorageChange', {
        detail: { key, value: defaultValue }
      })
    );
  };

  return {
    data,
    setData,
    removeData
  };
}
