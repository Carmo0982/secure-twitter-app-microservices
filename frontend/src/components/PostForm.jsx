import { useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { createPost, setAuthToken } from '../api'

export default function PostForm({ onPostCreated }) {
    const [content, setContent] = useState('')
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState('')
    const { getAccessTokenSilently } = useAuth0()

    async function handleSubmit(e) {
        e.preventDefault()
        if (!content.trim()) return

        setLoading(true)
        setError('')
        try {
            const token = await getAccessTokenSilently()
            setAuthToken(token)
            await createPost(content)
            setContent('')
            onPostCreated() // refresca el feed
        } catch (err) {
            setError(err.response?.data?.error || 'Error al crear el post')
        } finally {
            setLoading(false)
        }
    }

    return (
        <form onSubmit={handleSubmit} style={{ marginBottom: '2rem' }}>
      <textarea
          value={content}
          onChange={e => setContent(e.target.value)}
          placeholder="¿Qué está pasando?"
          maxLength={140}
          rows={3}
          style={{ width: '100%', padding: '0.75rem', fontSize: '1rem', resize: 'none', boxSizing: 'border-box' }}
      />
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginTop: '0.5rem' }}>
        <span style={{ fontSize: '0.85rem', color: content.length > 120 ? 'red' : '#6b7280' }}>
          {content.length}/140
        </span>
                {error && <span style={{ color: 'red', fontSize: '0.85rem' }}>{error}</span>}
                <button type="submit" disabled={loading || !content.trim()}>
                    {loading ? 'Publicando...' : 'Publicar'}
                </button>
            </div>
        </form>
    )
}