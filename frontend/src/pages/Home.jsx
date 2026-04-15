import { useState, useEffect, useCallback } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { getStream } from '../api'
import PostForm from '../components/PostForm'
import PostCard from '../components/PostCard'

export default function Home() {
    const { isAuthenticated } = useAuth0()
    const [posts, setPosts] = useState([])
    const [loading, setLoading] = useState(true)

    const fetchStream = useCallback(async () => {
        setLoading(true)
        try {
            const res = await getStream()
            setPosts(res.data.content)
        } catch {
            console.error('Error cargando el feed')
        } finally {
            setLoading(false)
        }
    }, [])

    useEffect(() => { fetchStream() }, [fetchStream])

    return (
        <div>
            <h2>Feed público</h2>

            {/* El formulario solo aparece si el usuario está autenticado */}
            {isAuthenticated && <PostForm onPostCreated={fetchStream} />}

            {loading ? (
                <p>Cargando posts...</p>
            ) : posts.length === 0 ? (
                <p style={{ color: '#6b7280' }}>No hay posts aún. ¡Sé el primero!</p>
            ) : (
                posts.map(post => (
                    <PostCard key={post.id} post={post} onDeleted={fetchStream} />
                ))
            )}
        </div>
    )
}