import axios from 'axios'

const API_URL = 'http://localhost:8081/v2/usuarios/login'

export async function login(username, password) {
    try {
        const response = await axios.post(API_URL, { username, password })
        const token = response.data
        // Guardamos el token
        localStorage.setItem('token', token)
        return token
    } catch (error) {
        throw error
    }
}

export function logout() {
    localStorage.removeItem('token')
}

export function getToken() {
    return localStorage.getItem('token')
}
