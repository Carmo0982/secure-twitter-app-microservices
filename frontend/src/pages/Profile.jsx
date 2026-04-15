import { useState, useEffect } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { getMe, getUserPosts, setAuthToken } from '../api'
import PostCard from '../components/PostCard'

export default function Profile() {
    const { getAccessTokenSilently } = useAuth0()
    const [profile, setProfile] = useState(null)
    const [posts, setPosts] = useState([])

    useEffect(() => {
        async function fetchProfileAndPosts() {
            try {
                const token = await getAccessTokenSilently()
                setAuthToken(token)

                const meRes = await getMe()
                setProfile(meRes.data)

                const postsRes = await getUserPosts(meRes.data.id)
                setPosts(postsRes.data.content)
            } catch (error) {
                console.error(error)
            }
        }

        fetchProfileAndPosts()
    }, [getAccessTokenSilently])

    if (!profile) return <p>Cargando perfil...</p>

    return (
        <div>
            <h2>@{profile.username}</h2>
            <p style={{ color: '#6b7280' }}>{profile.email}</p>
            <p style={{ color: '#6b7280', fontSize: '0.85rem' }}>
                Miembro desde {new Date(profile.createdAt).toLocaleDateString()}
            </p>

            <hr />
            <h3>Tus posts</h3>
            {posts.length === 0 ? (
                <p style={{ color: '#6b7280' }}>Aún no has publicado nada.</p>
            ) : (
                posts.map(post => (
                    <PostCard key={post.id} post={post} onDeleted={() =>
                        setPosts(prev => prev.filter(p => p.id !== post.id))
                    } />
                ))
            )}
        </div>
    )
}