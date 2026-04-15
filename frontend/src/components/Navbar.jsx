import { useEffect, useState } from 'react'
import { useAuth0 } from '@auth0/auth0-react'
import { Link } from 'react-router-dom'
import { setAuthToken, registerUser } from '../api'

export default function Navbar() {
    const { isAuthenticated, isLoading, loginWithRedirect, logout, getAccessTokenSilently, user, error } = useAuth0()
    const [isLoginRedirecting, setIsLoginRedirecting] = useState(false)

    async function handleLogin() {
        try {
            setIsLoginRedirecting(true)
            await loginWithRedirect({
                appState: { returnTo: window.location.pathname },
                authorizationParams: {
                    redirect_uri: window.location.origin,
                    audience: import.meta.env.VITE_AUTH0_AUDIENCE,
                },
            })
        } catch (error) {
            console.error('Error iniciando login con Auth0', error)
            alert('No se pudo iniciar sesión. Revisa VITE_AUTH0_* y la configuración de callback en Auth0.')
            setIsLoginRedirecting(false)
        }
    }
    useEffect(() => {
        if (!isAuthenticated) return

        getAccessTokenSilently().then(token => {
            setAuthToken(token)
            registerUser().catch(() => {}) // Si ya existe, no pasa nada
        })
    }, [isAuthenticated, getAccessTokenSilently])

    return (
        <nav style={{
            display: 'flex', alignItems: 'center', justifyContent: 'space-between',
            padding: '1rem 2rem', borderBottom: '1px solid #e5e7eb'
        }}>
            <Link to="/" style={{ fontWeight: 700, fontSize: '1.2rem', textDecoration: 'none' }}>
                🐦 Twitter
            </Link>

            <div style={{ display: 'flex', gap: '1rem', alignItems: 'center' }}>
                {!isLoading && (
                    isAuthenticated ? (
                        <>
                            <Link to="/profile" style={{ textDecoration: 'none' }}>
                                {user?.name || user?.email}
                            </Link>
                            <button onClick={() => logout({ logoutParams: { returnTo: window.location.origin } })}>
                                Logout
                            </button>
                        </>
                    ) : (
                        <button onClick={handleLogin} disabled={isLoginRedirecting}>
                            {isLoginRedirecting ? 'Redirigiendo...' : 'Login'}
                        </button>
                    )
                )}
            </div>

            {error && (
                <p style={{ color: '#b91c1c', fontSize: '0.9rem', margin: 0 }}>
                    Error Auth0: {error.message}
                </p>
            )}
        </nav>
    )
}