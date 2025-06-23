import axios from 'axios'
import router from "@/router";

// Crear instancia
const api = axios.create({
    baseURL: 'http://localhost:8081/v2', // base de tus APIs
})

// Agregar token a todas las peticiones si existe
api.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token) {
        config.headers.Authorization = `Bearer ${token}`
    }
    return config
})
api.interceptors.response.use(
    response => response,
    error => {
        if (error.response && error.response.status === 401) {
            localStorage.removeItem('token')
            router.replace({ name: 'Login' }) // o por nombre: router.replace({ name: 'Login' })
        }
        return Promise.reject(error)
    }
)
export default api
