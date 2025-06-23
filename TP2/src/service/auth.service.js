import axios from 'axios'

const API_URL = 'http://localhost:8081/v2/usuarios/login'

export async function login(username, password) {
    try {
        const response = await axios.post(API_URL, { username, password })

        const token = response.data
        localStorage.setItem('token', token)
        return token
    } catch (error) {
        // Imprimimos el error detallado por si hace falta
        console.error('Error en login (servicio):', error.response?.data || error.message)

        // Propagamos el error tal cual
        throw error
    }
}

export function logout() {
    localStorage.removeItem('token')
}

export function getToken() {
    return localStorage.getItem('token')
}
