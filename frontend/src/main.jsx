import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { Auth0Provider } from '@auth0/auth0-react'
import App from './App.jsx'
import './index.css'

const domain   = import.meta.env.VITE_AUTH0_DOMAIN
const clientId = import.meta.env.VITE_AUTH0_CLIENT_ID
const audience = import.meta.env.VITE_AUTH0_AUDIENCE
const redirectUri = window.location.origin
const missingAuth0Vars = [
    ['VITE_AUTH0_DOMAIN', domain],
    ['VITE_AUTH0_CLIENT_ID', clientId],
].filter(([, value]) => !value).map(([name]) => name)

if (missingAuth0Vars.length > 0) {
    createRoot(document.getElementById('root')).render(
        <StrictMode>
            <main style={{ maxWidth: 700, margin: '2rem auto', padding: '0 1rem', fontFamily: 'system-ui, sans-serif' }}>
                <h2>Configuracion de Auth0 incompleta</h2>
                <p>Faltan variables en el frontend para habilitar Login:</p>
                <pre style={{ background: '#f5f5f5', padding: '1rem', borderRadius: 8 }}>
{missingAuth0Vars.join('\n')}
                </pre>
                <p>Crea el archivo <code>frontend/.env</code> y vuelve a levantar Vite.</p>
            </main>
        </StrictMode>
    )
} else {
    const onRedirectCallback = (appState) => {
        const targetUrl = appState?.returnTo || window.location.pathname
        window.history.replaceState({}, document.title, targetUrl)
    }

    createRoot(document.getElementById('root')).render(
        <StrictMode>
            <Auth0Provider
                domain={domain}
                clientId={clientId}
                cacheLocation="localstorage"
                useRefreshTokens={true}
                onRedirectCallback={onRedirectCallback}
                authorizationParams={{
                    redirect_uri: redirectUri,
                    audience: audience,
                }}
            >
                <App />
            </Auth0Provider>
        </StrictMode>
    )
}
