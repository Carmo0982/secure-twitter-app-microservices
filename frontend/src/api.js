import axios from 'axios'

const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080'

// Crea una instancia de axios con la URL base del backend
const api = axios.create({ baseURL: BASE_URL })

// Función para inyectar el token en cada request
// Se llama desde los componentes pasando getAccessTokenSilently de Auth0
export function setAuthToken(token) {
    if (token) {
        api.defaults.headers.common['Authorization'] = `Bearer ${token}`
    } else {
        delete api.defaults.headers.common['Authorization']
    }
}

// Endpoints
export const registerUser  = ()              => api.post('/auth/register')
export const getMe         = ()              => api.get('/api/me')
export const getStream     = (page = 0)      => api.get(`/api/stream?page=${page}&size=20`)
export const createPost    = (content)       => api.post('/api/posts', { content })
export const deletePost    = (id)            => api.delete(`/api/posts/${id}`)
export const getUserPosts  = (id, page = 0)  => api.get(`/api/users/${id}/posts?page=${page}&size=20`)

export default api