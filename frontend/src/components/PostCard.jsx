import { useAuth0 } from '@auth0/auth0-react'
import { deletePost, setAuthToken } from '../api'

export default function PostCard({ post, onDeleted }) {
    const { user, isAuthenticated, getAccessTokenSilently } = useAuth0()

    // Muestra el botón de borrar solo si el post es del usuario autenticado
    const isOwner = isAuthenticated && user?.sub && post.user?.username &&
        user.email?.split('@')[0] === post.user.username

    async function handleDelete() {
        if (!confirm('¿Eliminar este post?')) return
        const token = await getAccessTokenSilently()
        setAuthToken(token)
        await deletePost(post.id)
        onDeleted()
    }

    return (
        <div style={{
            padding: '1rem', borderBottom: '1px solid #e5e7eb',
            display: 'flex', flexDirection: 'column', gap: '0.5rem'
        }}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <strong>@{post.user?.username}</strong>
                <span style={{ fontSize: '0.8rem', color: '#6b7280' }}>
          {new Date(post.createdAt).toLocaleString()}
        </span>
            </div>
            <p style={{ margin: 0 }}>{post.content}</p>
            {isOwner && (
                <button onClick={handleDelete} style={{ alignSelf: 'flex-end', color: 'red', background: 'none', border: 'none', cursor: 'pointer' }}>
                    Eliminar
                </button>
            )}
        </div>
    )
}